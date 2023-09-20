package ru.avalc.ordering.restaurant.service.domain.event;

import ru.avalc.ordering.restaurant.service.domain.entity.OrderApproval;
import ru.avalc.ordering.system.domain.valueobject.RestaurantID;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

public class OrderApprovedEvent extends OrderApprovalEvent {

    public OrderApprovedEvent(OrderApproval orderApproval, RestaurantID restaurantID, List<String> failureMessages, ZonedDateTime createdAt) {
        super(orderApproval, restaurantID, failureMessages, createdAt);
    }
}
