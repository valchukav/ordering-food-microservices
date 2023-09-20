package ru.avalc.payment.service.domain.outbox.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.outbox.OutboxMessage;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.system.domain.valueobject.PaymentStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Setter
public class OrderOutboxMessage extends OutboxMessage {

    private PaymentStatus paymentStatus;
    private SagaStatus sagaStatus;

    @Builder
    private OrderOutboxMessage(UUID id, UUID sagaID, ZonedDateTime createdAt, ZonedDateTime processedAt, String type, String payload, SagaStatus sagaStatus, OutboxStatus outboxStatus, int version, PaymentStatus paymentStatus) {
        super(id, sagaID, createdAt, processedAt, type, payload, outboxStatus, version);
        this.paymentStatus = paymentStatus;
        this.sagaStatus = sagaStatus;
    }
}
