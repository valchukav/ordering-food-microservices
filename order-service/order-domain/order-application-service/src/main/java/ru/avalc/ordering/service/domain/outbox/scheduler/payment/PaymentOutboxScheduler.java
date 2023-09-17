package ru.avalc.ordering.service.domain.outbox.scheduler.payment;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import ru.avalc.ordering.service.domain.outbox.scheduler.OrderOutboxScheduler;
import ru.avalc.ordering.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class PaymentOutboxScheduler extends OrderOutboxScheduler<OrderPaymentOutboxMessage> {

    public PaymentOutboxScheduler(PaymentOutboxHelper outboxHelper, PaymentRequestMessagePublisher requestMessagePublisher) {
        super(outboxHelper, requestMessagePublisher, OutboxStatus.STARTED, new SagaStatus[]{SagaStatus.STARTED, SagaStatus.COMPENSATING});
    }
}
