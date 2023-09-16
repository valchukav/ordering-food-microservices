package ru.avalc.ordering.service.domain.outbox.model.payment;

import lombok.Builder;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.outbox.model.OutboxMessage;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */


public class OrderPaymentOutboxMessage extends OutboxMessage {

    @Builder
    private OrderPaymentOutboxMessage(UUID id, UUID sagaID, ZonedDateTime createdAt, ZonedDateTime processedAt, String type, String payload, SagaStatus sagaStatus, OutboxStatus outboxStatus, int version) {
        super(id, sagaID, createdAt, processedAt, type, payload, sagaStatus, outboxStatus, version);
    }
}
