package ru.avalc.ordering.payment.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import ru.avalc.ordering.payment.service.dataaccess.outbox.repository.OrderOutboxJpaRepository;
import ru.avalc.ordering.payment.service.dto.PaymentRequest;
import ru.avalc.ordering.system.domain.valueobject.PaymentOrderStatus;
import ru.avalc.ordering.system.domain.valueobject.PaymentStatus;
import ru.avalc.ordering.tests.OrderingTest;
import ru.avalc.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.avalc.ordering.saga.order.SagaConstants.ORDER_SAGA_NAME;

/**
 * @author Alexei Valchuk, 19.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@SpringBootTest(classes = PaymentServiceApplication.class)
public class PaymentRequestMessageListenerIntegrationTest extends OrderingTest {

    @Autowired
    private PaymentRequestMessageListener paymentRequestMessageListener;

    @Autowired
    private OrderOutboxJpaRepository orderOutboxJpaRepository;

    private final BigDecimal price = BigDecimal.valueOf(100);

    @AfterEach
    public void clean() {
        orderOutboxJpaRepository.deleteAll();
    }

    @Test
    public void testDoublePayment() {
        String sagaID = UUID.randomUUID().toString();

        paymentRequestMessageListener.completePayment(getPaymentRequest(sagaID));
        try {
            paymentRequestMessageListener.completePayment(getPaymentRequest(sagaID));
        } catch (DataAccessException e) {
            log.error("DataAccessException occurred with sql state: {}",
                    ((PSQLException) Objects.requireNonNull(e.getRootCause())).getSQLState());
        }

        assertOrderOutbox(sagaID);
    }

    @Test
    public void testDoublePaymentWithThreads() {
        String sagaID = UUID.randomUUID().toString();
        ExecutorService executorService = null;

        try {
            executorService = Executors.newFixedThreadPool(2);
            List<Callable<Object>> tasks = new ArrayList<>();

            tasks.add(Executors.callable(() -> {
                try {
                    paymentRequestMessageListener.completePayment(getPaymentRequest(sagaID));
                } catch (DataAccessException e) {
                    log.error("DataAccessException occurred for thread 1 with sql state: {}",
                            ((PSQLException) Objects.requireNonNull(e.getRootCause())).getSQLState());
                }
            }));

            tasks.add(Executors.callable(() -> {
                try {
                    paymentRequestMessageListener.completePayment(getPaymentRequest(sagaID));
                } catch (DataAccessException e) {
                    log.error("DataAccessException occurred for thread 2 with sql state: {}",
                            ((PSQLException) Objects.requireNonNull(e.getRootCause())).getSQLState());
                }
            }));

            executorService.invokeAll(tasks);

            assertOrderOutbox(sagaID);
        } catch (InterruptedException e) {
            log.error("Error calling complete payment", e);
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }

    private void assertOrderOutbox(String sagaId) {
        Optional<OrderOutboxEntity> orderOutboxEntity = orderOutboxJpaRepository.findByTypeAndSagaIDAndPaymentStatusAndOutboxStatus(
                ORDER_SAGA_NAME,
                UUID.fromString(sagaId),
                PaymentStatus.COMPLETED,
                OutboxStatus.STARTED
        );

        assertThat(orderOutboxEntity).isPresent();
        assertThat(orderOutboxEntity.get().getSagaID().toString()).isEqualTo(sagaId);
    }

    private PaymentRequest getPaymentRequest(String sagaID) {
        return PaymentRequest.builder()
                .id(UUID.randomUUID().toString())
                .sagaID(sagaID)
                .orderID(UUID.randomUUID().toString())
                .paymentOrderStatus(PaymentOrderStatus.PENDING)
                .customerID(CUSTOMER_ID.toString())
                .price(price)
                .createdAt(Instant.now())
                .build();
    }
}
