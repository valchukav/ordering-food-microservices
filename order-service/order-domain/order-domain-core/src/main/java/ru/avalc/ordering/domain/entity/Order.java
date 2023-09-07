package ru.avalc.ordering.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.domain.valueobject.OrderItemID;
import ru.avalc.ordering.domain.valueobject.StreetAddress;
import ru.avalc.ordering.domain.valueobject.TrackingID;
import ru.avalc.ordering.system.domain.entity.AggregateRoot;
import ru.avalc.ordering.system.domain.valueobject.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 06.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class Order extends AggregateRoot<OrderID> {

    private final CustomerID customerID;
    private final RestaurantID restaurantID;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> orderItems;

    @Setter
    private TrackingID trackingId;
    @Setter
    private OrderStatus orderStatus;
    @Setter
    private List<String> failureMessages;

    @Builder
    private Order(OrderID orderID, CustomerID customerID, RestaurantID restaurantID, StreetAddress deliveryAddress,
                  Money price, List<OrderItem> orderItems, TrackingID trackingId, OrderStatus orderStatus, List<String> failureMessages) {
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
        trackingId = new TrackingID(UUID.randomUUID());
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

    public void pay() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in a correct state for pay operation");
        }

        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in a correct state for approve operation");
        }

        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages) {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in a correct state for init cancel operation");
        }

        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(List<String> failureMessages) {
        if (!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)) {
            throw new OrderDomainException("Order is not in a correct state for cancel operation");
        }

        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(mes -> !mes.isEmpty()).toList());
        }
        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
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
