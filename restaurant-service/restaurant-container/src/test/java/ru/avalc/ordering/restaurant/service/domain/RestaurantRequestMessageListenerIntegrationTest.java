package ru.avalc.ordering.restaurant.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.restaurant.service.dataaccess.outbox.entity.OrderOutboxEntity;
import ru.avalc.ordering.restaurant.service.dataaccess.outbox.repository.OrderOutboxJpaRepository;
import ru.avalc.ordering.restaurant.service.domain.entity.Product;
import ru.avalc.ordering.restaurant.service.domain.ports.input.RestaurantApprovalRequestMessageListener;
import ru.avalc.ordering.restaurant.service.dto.RestaurantApprovalRequest;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.ProductID;
import ru.avalc.ordering.system.domain.valueobject.RestaurantOrderStatus;
import ru.avalc.ordering.tests.OrderingTest;

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
@SpringBootTest(classes = RestaurantServiceApplication.class)
public class RestaurantRequestMessageListenerIntegrationTest extends OrderingTest {

    @Autowired
    private RestaurantApprovalRequestMessageListener restaurantApprovalRequestMessageListener;

    @Autowired
    private OrderOutboxJpaRepository orderOutboxJpaRepository;

    @AfterEach
    public void clean() {
        orderOutboxJpaRepository.deleteAll();
    }

    @Test
    public void testDoublePayment() {
        String sagaID = UUID.randomUUID().toString();

        restaurantApprovalRequestMessageListener.approveOrder(getRestaurantApprovalRequest(sagaID));
        try {
            restaurantApprovalRequestMessageListener.approveOrder(getRestaurantApprovalRequest(sagaID));
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
                    restaurantApprovalRequestMessageListener.approveOrder(getRestaurantApprovalRequest(sagaID));
                } catch (DataAccessException e) {
                    log.error("DataAccessException occurred for thread 1 with sql state: {}",
                            ((PSQLException) Objects.requireNonNull(e.getRootCause())).getSQLState());
                }
            }));

            tasks.add(Executors.callable(() -> {
                try {
                    restaurantApprovalRequestMessageListener.approveOrder(getRestaurantApprovalRequest(sagaID));
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
        Optional<OrderOutboxEntity> orderOutboxEntity = orderOutboxJpaRepository.findByTypeAndSagaIDAndOutboxStatus(
                ORDER_SAGA_NAME,
                UUID.fromString(sagaId),
                OutboxStatus.STARTED
        );

        assertThat(orderOutboxEntity).isPresent();
        assertThat(orderOutboxEntity.get().getSagaID().toString()).isEqualTo(sagaId);
    }

    private RestaurantApprovalRequest getRestaurantApprovalRequest(String sagaID) {
        Product product_1 = Product.builder()
                .productID(new ProductID(PRODUCT_ID_1))
                .name("prod_1")
                .price(new Money(50))
                .quantity(1)
                .available(true)
                .build();

        Product product_2 = Product.builder()
                .productID(new ProductID(PRODUCT_ID_2))
                .name("prod_2")
                .price(new Money(50))
                .quantity(3)
                .available(true)
                .build();

        return RestaurantApprovalRequest.builder()
                .id(UUID.randomUUID().toString())
                .sagaID(sagaID)
                .orderID(UUID.randomUUID().toString())
                .restaurantOrderStatus(RestaurantOrderStatus.PAID)
                .restaurantID(RESTAURANT_ID.toString())
                .products(new ArrayList<>(List.of(product_1, product_2)))
                .price(PRICE)
                .createdAt(Instant.now())
                .build();
    }
}
