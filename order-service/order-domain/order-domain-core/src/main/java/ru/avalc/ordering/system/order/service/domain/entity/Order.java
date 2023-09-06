package ru.avalc.ordering.system.order.service.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.system.domain.entity.AggregateRoot;
import ru.avalc.ordering.system.domain.valueobject.*;
import ru.avalc.ordering.system.order.service.domain.valueobject.StreetAddress;
import ru.avalc.ordering.system.order.service.domain.valueobject.TrackingId;

import java.util.List;

/**
 * @author Alexei Valchuk, 06.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Builder
public class Order extends AggregateRoot<OrderID> {

    private final CustomerID customerID;
    private final RestaurantID restaurantID;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> orderItems;

    @Setter
    private TrackingId trackingId;
    @Setter
    private OrderStatus orderStatus;
    @Setter
    private List<String> failureMessages;

    private Order(OrderID orderID, CustomerID customerID, RestaurantID restaurantID, StreetAddress deliveryAddress, Money price, List<OrderItem> orderItems, TrackingId trackingId, OrderStatus orderStatus, List<String> failureMessages) {
        super(orderID);
        this.customerID = customerID;
        this.restaurantID = restaurantID;
        this.deliveryAddress = deliveryAddress;
        this.price = price;
        this.orderItems = orderItems;
        this.trackingId = trackingId;
        this.orderStatus = orderStatus;
        this.failureMessages = failureMessages;
    }
}
