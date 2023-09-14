package ru.avalc.payment.service.domain.ports.output.repository.message.publisher;

import ru.avalc.ordering.payment.service.domain.event.PaymentFailedEvent;
import ru.avalc.ordering.system.domain.event.publisher.DomainEventPublisher;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

public interface PaymentFailedMessagePublisher extends DomainEventPublisher<PaymentFailedEvent> {

}