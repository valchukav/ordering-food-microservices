package ru.avalc.ordering.service.domain.outbox.model.approval;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.outbox.OutboxMessage;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

public class OrderApprovalOutboxMessage extends OutboxMessage {

    @Setter
    @Getter
    private OrderStatus orderStatus;

    @Builder
    private OrderApprovalOutboxMessage(UUID id, UUID sagaID, ZonedDateTime createdAt, ZonedDateTime processedAt, String type, String payload, SagaStatus sagaStatus, OutboxStatus outboxStatus, int version, OrderStatus orderStatus) {
        super(id, sagaID, createdAt, processedAt, type, payload, sagaStatus, outboxStatus, version);
        this.orderStatus = orderStatus;
    }
}
