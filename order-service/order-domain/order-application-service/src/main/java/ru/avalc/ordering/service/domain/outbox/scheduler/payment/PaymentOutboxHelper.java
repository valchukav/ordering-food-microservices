package ru.avalc.ordering.service.domain.outbox.scheduler.payment;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import ru.avalc.ordering.service.domain.outbox.scheduler.OutboxHelper;
import ru.avalc.ordering.service.domain.ports.output.repository.PaymentOutboxRepository;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class PaymentOutboxHelper extends OutboxHelper<OrderPaymentOutboxMessage> {

    public PaymentOutboxHelper(PaymentOutboxRepository outboxRepository) {
        super(outboxRepository);
    }
}
