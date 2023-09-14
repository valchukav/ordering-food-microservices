package ru.avalc.ordering.restaurant.service.domain.entity;

import lombok.Builder;
import lombok.Getter;
import ru.avalc.ordering.restaurant.service.domain.valueobject.OrderApprovalID;
import ru.avalc.ordering.system.domain.entity.BaseEntity;
import ru.avalc.ordering.system.domain.valueobject.OrderApprovalStatus;
import ru.avalc.ordering.system.domain.valueobject.OrderID;
import ru.avalc.ordering.system.domain.valueobject.RestaurantID;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class OrderApproval extends BaseEntity<OrderApprovalID> {

    private final RestaurantID restaurantID;
    private final OrderID orderID;
    private final OrderApprovalStatus orderApprovalStatus;

    @Builder
    private OrderApproval(OrderApprovalID orderApprovalID, RestaurantID restaurantID, OrderID orderID, OrderApprovalStatus orderApprovalStatus) {
        super(orderApprovalID);
        this.restaurantID = restaurantID;
        this.orderID = orderID;
        this.orderApprovalStatus = orderApprovalStatus;
    }
}
