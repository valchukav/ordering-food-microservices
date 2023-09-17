package ru.avalc.ordering.domain.event;

import ru.avalc.ordering.domain.entity.Order;

import java.time.ZonedDateTime;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

public class OrderCreatedEvent extends OrderEvent {

    public OrderCreatedEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
