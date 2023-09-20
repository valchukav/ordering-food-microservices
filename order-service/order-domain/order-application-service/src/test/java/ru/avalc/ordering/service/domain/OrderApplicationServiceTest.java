package ru.avalc.ordering.service.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.avalc.ordering.application.dto.create.CreateOrderResponse;
import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.entity.Product;
import ru.avalc.ordering.domain.entity.Restaurant;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.mapper.OrderDataMapper;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import ru.avalc.ordering.service.domain.ports.input.service.OrderApplicationService;
import ru.avalc.ordering.service.domain.ports.output.repository.*;
import ru.avalc.ordering.system.domain.valueobject.*;
import ru.avalc.ordering.tests.OrderingTest;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static ru.avalc.ordering.saga.order.SagaConstants.ORDER_SAGA_NAME;

/**
 * @author Alexei Valchuk, 09.09.2023, email: a.valchukav@gmail.com
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderConfigurationTest.class)
public class OrderApplicationServiceTest extends OrderingTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private PaymentOutboxRepository paymentOutboxRepository;

    @Autowired
    private ApprovalOutboxRepository approvalOutboxRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public void init() {
        order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderID(ORDER_ID));

        when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurant));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(paymentOutboxRepository.save(any(OrderPaymentOutboxMessage.class))).thenReturn(getOrderPaymentOutboxMessage());
    }

    private OrderPaymentOutboxMessage getOrderPaymentOutboxMessage() {
        OrderPaymentEventPayload orderPaymentEventPayload = OrderPaymentEventPayload.builder()
                .orderID(ORDER_ID.toString())
                .customerID(CUSTOMER_ID.toString())
                .price(PRICE)
                .createdAt(ZonedDateTime.now())
                .paymentOrderStatus(PaymentOrderStatus.PENDING.name())
                .build();

        return OrderPaymentOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaID(SAGA_ID)
                .createdAt(ZonedDateTime.now())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderPaymentEventPayload))
                .orderStatus(OrderStatus.PENDING)
                .sagaStatus(SagaStatus.STARTED)
                .outboxStatus(OutboxStatus.STARTED)
                .version(0)
                .build();
    }

    private String createPayload(OrderPaymentEventPayload orderPaymentEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderPaymentEventPayload);
        } catch (JsonProcessingException e) {
            throw new OrderDomainException("Cannot create OrderPaymentEventPayload object!");
        }
    }

    @Test
    public void testCreateOrder() {
        CreateOrderResponse orderResponse = orderApplicationService.createOrder(createOrderCommand);
        Assertions.assertAll(
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.PENDING),
                () -> assertThat(orderResponse.getMessage()).isNotEmpty(),
                () -> assertThat(orderResponse.getOrderTrackingID()).isNotNull()
        );
    }

    @Test
    public void testCreateOrderWithWrongTotalPrice() {
        OrderDomainException exception = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
        assertThat(exception.getMessage()).contains(List.of("price", PRICE.toString()));
    }

    @Test
    public void testCreateOrderWithWrongProductPrice() {
        OrderDomainException exception = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
        assertThat(exception.getMessage()).contains(List.of("price", "not valid"));
    }

    @Test
    public void testCreateOrderWithPassiveRestaurant() {
        Restaurant restaurant = Restaurant.builder()
                .restaurantID(new RestaurantID(RESTAURANT_ID))
                .active(false)
                .products(List.of(
                        Product.builder()
                                .productID(new ProductID(PRODUCT_ID_1))
                                .name("prod_1")
                                .price(new Money(50))
                                .build(),
                        Product.builder()
                                .productID(new ProductID(PRODUCT_ID_2))
                                .name("prod_2")
                                .price(new Money(50))
                                .build()
                ))
                .build();

        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurant));

        OrderDomainException exception = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommand));

        assertThat(exception.getMessage()).contains("not active");
    }
}
