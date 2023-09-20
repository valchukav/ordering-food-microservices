package ru.avalc.ordering.order.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;
import ru.avalc.ordering.application.dto.message.PaymentResponse;
import ru.avalc.ordering.order.service.dataaccess.outbox.approval.entity.ApprovalOutboxEntity;
import ru.avalc.ordering.order.service.dataaccess.outbox.approval.repository.ApprovalOutboxJpaRepository;
import ru.avalc.ordering.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import ru.avalc.ordering.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.OrderPaymentSaga;
import ru.avalc.ordering.tests.OrderingTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.avalc.ordering.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static ru.avalc.ordering.system.domain.valueobject.PaymentStatus.COMPLETED;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@SpringBootTest(classes = OrderServiceApplication.class)
@Sql(value = "classpath:sql/OrderPaymentSagaTestSetUp.sql")
@Sql(value = "classpath:sql/OrderPaymentSagaTestCleanUp.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderSagaIntegrationTest extends OrderingTest {

    @Autowired
    private OrderPaymentSaga orderPaymentSaga;

    @Autowired
    private PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    @Autowired
    private ApprovalOutboxJpaRepository approvalOutboxJpaRepository;

    private final BigDecimal price = BigDecimal.valueOf(100);

    @Test
    public void testDoublePayment() {
        orderPaymentSaga.process(getPaymentResponse());
        orderPaymentSaga.process(getPaymentResponse());

        assertPaymentOutbox();
        assertRestaurantApprovalOutbox();
    }

    @Test
    public void testDoublePaymentWithThreads() throws InterruptedException {
        Thread thread1 = new Thread(() -> orderPaymentSaga.process(getPaymentResponse()));
        Thread thread2 = new Thread(() -> orderPaymentSaga.process(getPaymentResponse()));

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertPaymentOutbox();
        assertRestaurantApprovalOutbox();
    }

    @Test
    public void testDoublePaymentWithLatch() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        Thread thread1 = new Thread(() -> {
            try {
                orderPaymentSaga.process(getPaymentResponse());
            } catch (OptimisticLockingFailureException e) {
                log.error("OptimisticLockingFailureException occurred for thread1");
            } finally {
                latch.countDown();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                orderPaymentSaga.process(getPaymentResponse());
            } catch (OptimisticLockingFailureException e) {
                log.error("OptimisticLockingFailureException occurred for thread2");
            } finally {
                latch.countDown();
            }
        });

        thread1.start();
        thread2.start();

        latch.await();

        assertPaymentOutbox();
        assertRestaurantApprovalOutbox();
    }

    public PaymentResponse getPaymentResponse() {
        return PaymentResponse.builder()
                .id(UUID.randomUUID().toString())
                .sagaID(SAGA_ID.toString())
                .paymentStatus(COMPLETED)
                .paymentID(PAYMENT_ID.toString())
                .orderID(ORDER_ID.toString())
                .customerID(CUSTOMER_ID.toString())
                .price(price)
                .createdAt(Instant.now())
                .failureMessages(new ArrayList<>())
                .build();
    }

    private void assertPaymentOutbox() {
        Optional<List<PaymentOutboxEntity>> outboxPaymentEntities
                = paymentOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(
                ORDER_SAGA_NAME,
                OutboxStatus.STARTED,
                List.of(SagaStatus.PROCESSING));

        assertAll(
                () -> assertThat(outboxPaymentEntities).isNotNull(),
                () -> assertThat(outboxPaymentEntities).isPresent(),
                () -> assertThat(outboxPaymentEntities.get().size()).isEqualTo(1)
        );
    }

    private void assertRestaurantApprovalOutbox() {
        Optional<List<ApprovalOutboxEntity>> outboxApprovalEntities
                = approvalOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(
                ORDER_SAGA_NAME,
                OutboxStatus.STARTED,
                List.of(SagaStatus.PROCESSING));

        assertAll(
                () -> assertThat(outboxApprovalEntities).isNotNull(),
                () -> assertThat(outboxApprovalEntities).isPresent(),
                () -> assertThat(outboxApprovalEntities.get().size()).isEqualTo(1)
        );
    }
}
