package ru.avalc.payment.service.domain.ports.output.repository.message.publisher;

import ru.avalc.ordering.payment.service.domain.event.PaymentCompletedEvent;
import ru.avalc.ordering.system.domain.event.publisher.DomainEventPublisher;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

public interface PaymentCompletedMessagePublisher extends DomainEventPublisher<PaymentCompletedEvent> {

}
