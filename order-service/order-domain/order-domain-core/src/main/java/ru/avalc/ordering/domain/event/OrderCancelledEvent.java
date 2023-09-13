package ru.avalc.ordering.domain.event;


import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.system.domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

public class OrderCancelledEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCancelledEvent> domainEventPublisher;

    public OrderCancelledEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<OrderCancelledEvent> domainEventPublisher) {
        super(order, createdAt);
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void fire() {
        domainEventPublisher.publish(this);
    }
}
