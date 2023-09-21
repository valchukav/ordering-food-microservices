package ru.avalc.ordering.customer.service.domain.ports.output.message.publisher;

import ru.avalc.customer.service.domain.event.CustomerCreatedEvent;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

public interface CustomerMessagePublisher {

    void publish(CustomerCreatedEvent customerCreatedEvent);
}
