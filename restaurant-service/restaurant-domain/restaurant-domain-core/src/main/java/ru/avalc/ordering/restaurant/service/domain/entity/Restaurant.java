package ru.avalc.ordering.restaurant.service.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.restaurant.service.domain.valueobject.OrderApprovalID;
import ru.avalc.ordering.system.domain.entity.AggregateRoot;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.OrderApprovalStatus;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;
import ru.avalc.ordering.system.domain.valueobject.RestaurantID;

import java.util.List;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Setter
public class Restaurant extends AggregateRoot<RestaurantID> {

    private OrderApproval orderApproval;
    private boolean active;
    private OrderDetail orderDetail;

    public void validateOrder(List<String> failureMessages) {
        if (orderDetail.getOrderStatus() != OrderStatus.PAID) {
            failureMessages.add("Payment is not completed for order: " + orderDetail.getId().getValue());
        }

        Money totalAmount = orderDetail.getProducts().stream().map(product -> {
            if (!product.isAvailable()) {
                failureMessages.add("Product with id: " + product.getId().getValue() + " is not available");
            }
            return product.getPrice().multiply(product.getQuantity());
        }).reduce(Money.ZERO, Money::add);

        if (!totalAmount.equals(orderDetail.getTotalAmount())) {
            failureMessages.add("Total price is not correct for order: " + orderDetail.getId().getValue());
        }
    }

    public void constructOrderApproval(OrderApprovalStatus orderApprovalStatus) {
        this.orderApproval = OrderApproval.builder()
                .orderApprovalID(new OrderApprovalID(UUID.randomUUID()))
                .restaurantID(this.getId())
                .orderID(this.getOrderDetail().getId())
                .orderApprovalStatus(orderApprovalStatus)
                .build();
    }

    @Builder
    private Restaurant(RestaurantID restaurantID, OrderApproval orderApproval, boolean active, OrderDetail orderDetail) {
        super(restaurantID);
        this.orderApproval = orderApproval;
        this.active = active;
        this.orderDetail = orderDetail;
    }
}
