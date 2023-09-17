package ru.avalc.ordering.service.domain.outbox.scheduler.payment;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import ru.avalc.ordering.service.domain.outbox.scheduler.OrderOutboxCleanerScheduler;

/**
 * @author Alexei Valchuk, 17.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class PaymentOutboxCleanerScheduler extends OrderOutboxCleanerScheduler<OrderPaymentOutboxMessage> {

    public PaymentOutboxCleanerScheduler(PaymentOutboxHelper outboxHelper) {
        super(outboxHelper);
    }
}
