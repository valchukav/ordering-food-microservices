package ru.avalc.ordering.system.order.service.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.system.domain.entity.AggregateRoot;
import ru.avalc.ordering.system.domain.valueobject.*;
import ru.avalc.ordering.system.order.service.domain.exception.OrderDomainException;
import ru.avalc.ordering.system.order.service.domain.valueobject.OrderItemID;
import ru.avalc.ordering.system.order.service.domain.valueobject.StreetAddress;
import ru.avalc.ordering.system.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;

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

    public void initOrder() {
        super.setId(new OrderID(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initOrderItems();
    }

    private void initOrderItems() {
        long itemID = 1L;
        for (OrderItem orderItem : orderItems) {
            orderItem.initOrderItem(super.getId(), new OrderItemID(itemID++));
        }
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateInitialOrder() {
        if (orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in a correct state for initialization");
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero");
        }
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = orderItems.stream().map(orderItem -> {
            validateOrderItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if (!price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: " + price.getAmount()
                    + ", is not equal to Order items total: " + orderItemsTotal.getAmount());
        }
    }

    private void validateOrderItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) throw new OrderDomainException("Item price: " + orderItem.getPrice().getAmount()
                + ", is not valid for product: " + orderItem.getProduct().getId().getValue());
    }
}
