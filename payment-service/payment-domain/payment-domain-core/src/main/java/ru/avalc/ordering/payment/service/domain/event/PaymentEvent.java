package ru.avalc.ordering.payment.service.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.avalc.ordering.payment.service.domain.entity.Payment;
import ru.avalc.ordering.system.domain.event.DomainEvent;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@AllArgsConstructor
public abstract class PaymentEvent implements DomainEvent<Payment> {

    private final Payment payment;
    private final ZonedDateTime createdAt;
    private final List<String> failureMessages;
}
