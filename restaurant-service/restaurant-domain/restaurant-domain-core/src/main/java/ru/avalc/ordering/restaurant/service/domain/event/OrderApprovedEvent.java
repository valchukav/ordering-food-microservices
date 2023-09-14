package ru.avalc.ordering.restaurant.service.domain.event;

import ru.avalc.ordering.restaurant.service.domain.entity.OrderApproval;
import ru.avalc.ordering.system.domain.event.publisher.DomainEventPublisher;
import ru.avalc.ordering.system.domain.valueobject.RestaurantID;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

public class OrderApprovedEvent extends OrderApprovalEvent {

    private final DomainEventPublisher<OrderApprovalEvent> domainEventPublisher;

    public OrderApprovedEvent(OrderApproval orderApproval, RestaurantID restaurantID, List<String> failureMessages, ZonedDateTime createdAt, DomainEventPublisher<OrderApprovalEvent> domainEventPublisher) {
        super(orderApproval, restaurantID, failureMessages, createdAt);
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void fire() {
        domainEventPublisher.publish(this);
    }
}
