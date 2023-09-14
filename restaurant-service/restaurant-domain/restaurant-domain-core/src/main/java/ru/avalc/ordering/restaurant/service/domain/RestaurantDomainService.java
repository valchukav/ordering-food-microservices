package ru.avalc.ordering.restaurant.service.domain;

import ru.avalc.ordering.restaurant.service.domain.entity.Restaurant;
import ru.avalc.ordering.restaurant.service.domain.event.OrderApprovalEvent;
import ru.avalc.ordering.restaurant.service.domain.event.OrderRejectedEvent;
import ru.avalc.ordering.system.domain.event.publisher.DomainEventPublisher;

import java.util.List;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

public interface RestaurantDomainService {

    OrderApprovalEvent validateOrder(Restaurant restaurant, List<String> failureMessages,
                                     DomainEventPublisher<OrderApprovalEvent> orderApprovalEventDomainEventPublisher,
                                     DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher);
}
