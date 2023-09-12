package ru.avalc.ordering.order.service.dataaccess.order.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.entity.OrderItem;
import ru.avalc.ordering.domain.entity.Product;
import ru.avalc.ordering.domain.valueobject.OrderItemID;
import ru.avalc.ordering.domain.valueobject.StreetAddress;
import ru.avalc.ordering.domain.valueobject.TrackingID;
import ru.avalc.ordering.order.service.dataaccess.order.entity.OrderAddressEntity;
import ru.avalc.ordering.order.service.dataaccess.order.entity.OrderEntity;
import ru.avalc.ordering.order.service.dataaccess.order.entity.OrderItemEntity;
import ru.avalc.ordering.system.domain.valueobject.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.avalc.ordering.order.service.dataaccess.order.entity.OrderEntity.FAILURE_MESSAGE_DELIMITER;
import static ru.avalc.ordering.order.service.dataaccess.order.entity.OrderEntity.builder;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class OrderDataAccessMapper {

    public OrderEntity orderToOrderEntity(Order order) {
        OrderEntity orderEntity = builder()
                .id(order.getId().getValue())
                .customerID(order.getCustomerID().getValue())
                .restaurantID(order.getRestaurantID().getValue())
                .trackingID(order.getTrackingId().getValue())
                .orderAddress(deliveryAddressToAddressEntity(order.getDeliveryAddress()))
                .price(order.getPrice().getAmount())
                .items(orderItemsToOrderItemEntities(order.getOrderItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages() != null ?
                        String.join(FAILURE_MESSAGE_DELIMITER, order.getFailureMessages()) : "")
                .build();

        orderEntity.getOrderAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));

        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .orderID(new OrderID(orderEntity.getId()))
                .customerID(new CustomerID(orderEntity.getCustomerID()))
                .restaurantID(new RestaurantID(orderEntity.getRestaurantID()))
                .deliveryAddress(addressEntityToDeliveryAddress(orderEntity.getOrderAddress()))
                .price(new Money(orderEntity.getPrice()))
                .orderItems(orderItemEntitiesToOrderItems(orderEntity.getItems()))
                .trackingId(new TrackingID(orderEntity.getTrackingID()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessages(orderEntity.getFailureMessages().isEmpty() ? new ArrayList<>() :
                        new ArrayList<>(Arrays.asList(orderEntity.getFailureMessages().split(FAILURE_MESSAGE_DELIMITER))))
                .build();
    }

    private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .id(deliveryAddress.getId())
                .city(deliveryAddress.getCity())
                .postalCode(deliveryAddress.getPostalCode())
                .build();
    }

    private List<OrderItemEntity> orderItemsToOrderItemEntities(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> OrderItemEntity.builder()
                        .id(item.getId().getValue())
                        .productID(item.getProduct().getId().getValue())
                        .price(item.getPrice().getAmount())
                        .quantity(item.getQuantity())
                        .subTotal(item.getSubTotal().getAmount())
                        .build()).collect(Collectors.toList());
    }

    private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity orderAddress) {
        return new StreetAddress(orderAddress.getId(), orderAddress.getStreet(), orderAddress.getPostalCode(), orderAddress.getCity());
    }

    private List<OrderItem> orderItemEntitiesToOrderItems(List<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.stream()
                .map(item -> OrderItem.builder()
                        .orderItemID(new OrderItemID(item.getId()))
                        .product(Product.builder().productID(new ProductID(item.getProductID())).build())
                        .price(new Money(item.getPrice()))
                        .quantity(item.getQuantity())
                        .subTotal(new Money(item.getSubTotal()))
                        .build()).collect(Collectors.toList());
    }
}
