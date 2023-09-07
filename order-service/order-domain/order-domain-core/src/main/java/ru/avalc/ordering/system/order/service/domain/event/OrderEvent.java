package ru.avalc.ordering.system.order.service.domain.event;

import lombok.Getter;
import ru.avalc.ordering.system.domain.event.DomainEvent;
import ru.avalc.ordering.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public abstract class OrderEvent implements DomainEvent<Order> {

    private final Order order;
    private final ZonedDateTime createdAt;

    public OrderEvent(Order order, ZonedDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }
}
