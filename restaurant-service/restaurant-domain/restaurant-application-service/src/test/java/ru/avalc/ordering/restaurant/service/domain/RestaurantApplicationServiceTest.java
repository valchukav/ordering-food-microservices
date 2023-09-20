package ru.avalc.ordering.restaurant.service.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.restaurant.service.domain.entity.OrderApproval;
import ru.avalc.ordering.restaurant.service.domain.entity.OrderDetail;
import ru.avalc.ordering.restaurant.service.domain.entity.Product;
import ru.avalc.ordering.restaurant.service.domain.entity.Restaurant;
import ru.avalc.ordering.restaurant.service.domain.event.OrderApprovalEvent;
import ru.avalc.ordering.restaurant.service.domain.event.OrderApprovedEvent;
import ru.avalc.ordering.restaurant.service.domain.event.OrderRejectedEvent;
import ru.avalc.ordering.restaurant.service.domain.exception.RestaurantNotFoundException;
import ru.avalc.ordering.restaurant.service.domain.outbox.model.OrderEventPayload;
import ru.avalc.ordering.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import ru.avalc.ordering.restaurant.service.domain.ports.output.OrderApprovalRepository;
import ru.avalc.ordering.restaurant.service.domain.ports.output.OrderOutboxRepository;
import ru.avalc.ordering.restaurant.service.domain.ports.output.RestaurantRepository;
import ru.avalc.ordering.restaurant.service.dto.RestaurantApprovalRequest;
import ru.avalc.ordering.system.domain.valueobject.*;
import ru.avalc.ordering.tests.OrderingTest;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static ru.avalc.ordering.saga.order.SagaConstants.ORDER_SAGA_NAME;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@SpringBootTest(classes = RestaurantConfigurationTest.class)
public class RestaurantApplicationServiceTest extends OrderingTest {

    @Autowired
    private RestaurantApprovalRequestHelper restaurantApprovalRequestHelper;

    @Autowired
    private RestaurantDomainService restaurantDomainService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderApprovalRepository orderApprovalRepository;

    @Autowired
    private OrderOutboxRepository orderOutboxRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderDetail orderDetailWithDisabledProduct;
    private OrderDetail orderDetailWithWrongTotalPrice;

    private Restaurant restaurant;

    private RestaurantApprovalRequest restaurantApprovalRequest;

    @BeforeEach
    public void init() {
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

        Money totalPrice = product_1.getPrice().multiply(product_1.getQuantity()).add(product_2.getPrice().multiply(product_2.getQuantity()));

        OrderDetail orderDetail = OrderDetail.builder()
                .orderID(new OrderID(ORDER_ID))
                .orderStatus(OrderStatus.PAID)
                .products(new ArrayList<>(List.of(product_1, product_2)))
                .totalAmount(totalPrice)
                .build();

        Product disabledProduct = Product.builder()
                .productID(new ProductID(PRODUCT_ID_2))
                .name("prod_2")
                .price(new Money(50))
                .quantity(3)
                .available(false)
                .build();

        orderDetailWithDisabledProduct = OrderDetail.builder()
                .orderID(new OrderID(ORDER_ID))
                .orderStatus(OrderStatus.PAID)
                .products(new ArrayList<>(List.of(product_1, disabledProduct)))
                .totalAmount(totalPrice)
                .build();

        orderDetailWithWrongTotalPrice = OrderDetail.builder()
                .orderID(new OrderID(ORDER_ID))
                .orderStatus(OrderStatus.PAID)
                .products(new ArrayList<>(List.of(product_1, product_2)))
                .totalAmount(totalPrice.multiply(12))
                .build();

        restaurant = Restaurant.builder()
                .restaurantID(new RestaurantID(RESTAURANT_ID))
                .active(true)
                .orderDetail(orderDetail)
                .build();

        restaurantApprovalRequest = RestaurantApprovalRequest.builder()
                .id(UUID.randomUUID().toString())
                .sagaID(SAGA_ID.toString())
                .restaurantID(RESTAURANT_ID.toString())
                .orderID(ORDER_ID.toString())
                .products(new ArrayList<>(List.of(product_1, product_2)))
                .price(totalPrice.getAmount())
                .restaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();

        when(restaurantRepository.findRestaurantInformation(any(Restaurant.class))).thenReturn(Optional.of(restaurant));
        when(orderApprovalRepository.save(any(OrderApproval.class))).thenReturn(null);
        when(orderOutboxRepository.save(any(OrderOutboxMessage.class))).thenReturn(getOrderOutboxMessage());
    }

    @Test
    public void validateOrder() {
        OrderApprovalEvent orderApprovalEvent = restaurantDomainService.validateOrder(restaurant, new ArrayList<>());

        assertAll(
                () -> assertThat(orderApprovalEvent.getFailureMessages()).isEmpty(),
                () -> assertThat(orderApprovalEvent).isInstanceOf(OrderApprovedEvent.class),
                () -> assertThat(orderApprovalEvent.getOrderApproval().getOrderApprovalStatus()).isEqualTo(OrderApprovalStatus.APPROVED)
        );
    }

    @Test
    public void validateOrderWithDisabledProduct() {
        Restaurant restaurantWithDisabledProduct = restaurant;
        restaurantWithDisabledProduct.setOrderDetail(orderDetailWithDisabledProduct);

        OrderApprovalEvent orderApprovalEvent = restaurantDomainService.validateOrder(restaurantWithDisabledProduct, new ArrayList<>());

        assertAll(
                () -> assertThat(orderApprovalEvent.getFailureMessages()).isNotEmpty(),
                () -> assertThat(orderApprovalEvent).isInstanceOf(OrderRejectedEvent.class),
                () -> assertThat(orderApprovalEvent.getOrderApproval().getOrderApprovalStatus()).isEqualTo(OrderApprovalStatus.REJECTED)
        );
    }

    @Test
    public void validateOrderWithWrongTotalAmountOrder() {
        Restaurant restaurantWithWrongTotalAmount = restaurant;
        restaurantWithWrongTotalAmount.setOrderDetail(orderDetailWithWrongTotalPrice);
        OrderApprovalEvent orderApprovalEvent = restaurantDomainService.validateOrder(restaurantWithWrongTotalAmount, new ArrayList<>());

        assertAll(
                () -> assertThat(orderApprovalEvent.getFailureMessages()).isNotEmpty(),
                () -> assertThat(orderApprovalEvent).isInstanceOf(OrderRejectedEvent.class),
                () -> assertThat(orderApprovalEvent.getOrderApproval().getOrderApprovalStatus()).isEqualTo(OrderApprovalStatus.REJECTED)
        );
    }

    @Test
    public void persistOrderApprovalRestaurantNotFound() {
        when(restaurantRepository.findRestaurantInformation(any(Restaurant.class))).thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class, () -> restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest));
    }

    @Test
    public void cancelPaymentWhenMessageWithSagaIDIsAlreadyExists() {
        when(orderOutboxRepository.findByTypeAndSagaIdAndOutboxStatus(ORDER_SAGA_NAME, SAGA_ID,
                OutboxStatus.COMPLETED)).thenReturn(Optional.empty());

        restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest);

        verify(mock(RestaurantDomainServiceImpl.class), times(0))
                .validateOrder(any(Restaurant.class), anyList());
    }

    private OrderOutboxMessage getOrderOutboxMessage() {
        OrderEventPayload orderEventPayload = OrderEventPayload.builder()
                .orderID(ORDER_ID.toString())
                .restaurantID(RESTAURANT_ID.toString())
                .createdAt(ZonedDateTime.now())
                .orderApprovalStatus(OrderApprovalStatus.APPROVED.name())
                .build();

        return OrderOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaID(SAGA_ID)
                .createdAt(ZonedDateTime.now())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderEventPayload))
                .orderApprovalStatus(OrderApprovalStatus.APPROVED)
                .outboxStatus(OutboxStatus.STARTED)
                .version(0)
                .build();
    }

    private String createPayload(OrderEventPayload orderEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderEventPayload);
        } catch (JsonProcessingException e) {
            throw new OrderDomainException("Cannot create OrderPaymentEventPayload object!");
        }
    }
}
