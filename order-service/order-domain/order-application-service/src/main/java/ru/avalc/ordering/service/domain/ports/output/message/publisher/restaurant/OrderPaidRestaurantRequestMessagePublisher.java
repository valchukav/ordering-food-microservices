package ru.avalc.ordering.service.domain.ports.output.message.publisher.restaurant;

import ru.avalc.ordering.domain.event.OrderPaidEvent;
import ru.avalc.ordering.system.domain.event.publisher.DomainEventPublisher;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {

}
