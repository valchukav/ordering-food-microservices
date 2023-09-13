package ru.avalc.ordering.domain;

import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.entity.Restaurant;
import ru.avalc.ordering.domain.event.OrderCancelledEvent;
import ru.avalc.ordering.domain.event.OrderCreatedEvent;
import ru.avalc.ordering.domain.event.OrderPaidEvent;
import ru.avalc.ordering.system.domain.event.publisher.DomainEventPublisher;

import java.util.List;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

public interface OrderDomainService {

    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant, DomainEventPublisher<OrderCreatedEvent> domainEventPublisher);

    OrderPaidEvent payOrder(Order order, DomainEventPublisher<OrderPaidEvent> domainEventPublisher);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages, DomainEventPublisher<OrderCancelledEvent> domainEventPublisher);

    void cancelOrder(Order order, List<String> failureMessages);
}
