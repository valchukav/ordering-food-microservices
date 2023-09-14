package ru.avalc.ordering.restaurant.service.domain.ports.output;

import ru.avalc.ordering.restaurant.service.domain.event.OrderApprovedEvent;
import ru.avalc.ordering.system.domain.event.publisher.DomainEventPublisher;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovedEvent> {

}
