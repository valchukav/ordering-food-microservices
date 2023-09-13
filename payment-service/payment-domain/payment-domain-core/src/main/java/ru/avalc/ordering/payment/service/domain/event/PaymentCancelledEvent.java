package ru.avalc.ordering.payment.service.domain.event;

import ru.avalc.ordering.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

public class PaymentCancelledEvent extends PaymentEvent {

    public PaymentCancelledEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages) {
        super(payment, createdAt, failureMessages);
    }
}
