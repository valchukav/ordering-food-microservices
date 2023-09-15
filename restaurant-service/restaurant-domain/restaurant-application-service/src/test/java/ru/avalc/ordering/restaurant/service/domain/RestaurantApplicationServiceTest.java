package ru.avalc.ordering.restaurant.service.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.avalc.ordering.restaurant.service.domain.entity.OrderApproval;
import ru.avalc.ordering.restaurant.service.domain.entity.OrderDetail;
import ru.avalc.ordering.restaurant.service.domain.entity.Product;
import ru.avalc.ordering.restaurant.service.domain.entity.Restaurant;
import ru.avalc.ordering.restaurant.service.domain.event.OrderApprovalEvent;
import ru.avalc.ordering.restaurant.service.domain.event.OrderApprovedEvent;
import ru.avalc.ordering.restaurant.service.domain.event.OrderRejectedEvent;
import ru.avalc.ordering.restaurant.service.domain.exception.RestaurantNotFoundException;
import ru.avalc.ordering.restaurant.service.domain.ports.output.OrderApprovalRepository;
import ru.avalc.ordering.restaurant.service.domain.ports.output.RestaurantRepository;
import ru.avalc.ordering.restaurant.service.dto.RestaurantApprovalRequest;
import ru.avalc.ordering.system.domain.valueobject.*;
import ru.avalc.ordering.tests.OrderingTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@SpringBootTest(classes = RestaurantConfigurationTest.class)
public class RestaurantApplicationServiceTest extends OrderingTest {

    @Autowired
    private RestaurantApprovalRequestHelper restaurantApprovalRequestHelper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderApprovalRepository orderApprovalRepository;

    private OrderDetail orderDetailWithDisabledProduct;

    private Restaurant restaurant;

    private RestaurantApprovalRequest restaurantApprovalRequest;
    private RestaurantApprovalRequest restaurantApprovalRequestWithWrongTotalAmount;

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

        restaurant = Restaurant.builder()
                .restaurantID(new RestaurantID(RESTAURANT_ID))
                .active(true)
                .orderDetail(orderDetail)
                .build();

        restaurantApprovalRequest = RestaurantApprovalRequest.builder()
                .id(UUID.randomUUID().toString())
                .restaurantID(RESTAURANT_ID.toString())
                .orderID(ORDER_ID.toString())
                .products(new ArrayList<>(List.of(product_1, product_2)))
                .price(totalPrice.getAmount())
                .restaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();

        restaurantApprovalRequestWithWrongTotalAmount = RestaurantApprovalRequest.builder()
                .id(UUID.randomUUID().toString())
                .restaurantID(RESTAURANT_ID.toString())
                .orderID(ORDER_ID.toString())
                .products(new ArrayList<>(List.of(product_1, product_2)))
                .price(BigDecimal.ONE)
                .restaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();

        when(restaurantRepository.findRestaurantInformation(any(Restaurant.class))).thenReturn(Optional.of(restaurant));
        when(orderApprovalRepository.save(any(OrderApproval.class))).thenReturn(null);
    }

    @Test
    public void persistOrderApproval() {
        OrderApprovalEvent orderApprovalEvent = restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest);

        assertAll(
                () -> assertThat(orderApprovalEvent.getFailureMessages()).isEmpty(),
                () -> assertThat(orderApprovalEvent).isInstanceOf(OrderApprovedEvent.class),
                () -> assertThat(orderApprovalEvent.getOrderApproval().getOrderApprovalStatus()).isEqualTo(OrderApprovalStatus.APPROVED)
        );
    }

    @Test
    public void persistOrderApprovalRestaurantNotFound() {
        when(restaurantRepository.findRestaurantInformation(any(Restaurant.class))).thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class, () -> restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest));
    }

    @Test
    public void persistOrderApprovalWithDisabledProduct() {
        Restaurant restaurantWithDisabledProduct = restaurant;
        restaurantWithDisabledProduct.setOrderDetail(orderDetailWithDisabledProduct);
        when(restaurantRepository.findRestaurantInformation(any(Restaurant.class))).thenReturn(Optional.of(restaurantWithDisabledProduct));

        OrderApprovalEvent orderApprovalEvent = restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest);

        assertAll(
                () -> assertThat(orderApprovalEvent.getFailureMessages()).isNotEmpty(),
                () -> assertThat(orderApprovalEvent).isInstanceOf(OrderRejectedEvent.class),
                () -> assertThat(orderApprovalEvent.getOrderApproval().getOrderApprovalStatus()).isEqualTo(OrderApprovalStatus.REJECTED)
        );
    }

    @Test
    public void persistOrderApprovalWithWrongTotalAmountOrder() {
        OrderApprovalEvent orderApprovalEvent = restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequestWithWrongTotalAmount);

        assertAll(
                () -> assertThat(orderApprovalEvent.getFailureMessages()).isNotEmpty(),
                () -> assertThat(orderApprovalEvent).isInstanceOf(OrderRejectedEvent.class),
                () -> assertThat(orderApprovalEvent.getOrderApproval().getOrderApprovalStatus()).isEqualTo(OrderApprovalStatus.REJECTED)
        );
    }
}
