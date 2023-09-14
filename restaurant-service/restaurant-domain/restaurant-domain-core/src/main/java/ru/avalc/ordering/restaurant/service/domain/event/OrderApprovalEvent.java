package ru.avalc.ordering.restaurant.service.domain.event;

import lombok.Getter;
import ru.avalc.ordering.restaurant.service.domain.entity.OrderApproval;
import ru.avalc.ordering.system.domain.event.DomainEvent;
import ru.avalc.ordering.system.domain.valueobject.RestaurantID;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public abstract class OrderApprovalEvent implements DomainEvent<OrderApproval> {

    private final OrderApproval orderApproval;
    private final RestaurantID restaurantID;
    private final List<String> failureMessages;
    private final ZonedDateTime createdAt;

    public OrderApprovalEvent(OrderApproval orderApproval, RestaurantID restaurantID, List<String> failureMessages, ZonedDateTime createdAt) {
        this.orderApproval = orderApproval;
        this.restaurantID = restaurantID;
        this.failureMessages = failureMessages;
        this.createdAt = createdAt;
    }
}
