package ru.avalc.ordering.restaurant.service.domain.outbox.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.outbox.OutboxMessage;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.system.domain.valueobject.OrderApprovalStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 19.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Setter
public class OrderOutboxMessage extends OutboxMessage {

    private OrderApprovalStatus orderApprovalStatus;

    @Builder
    private OrderOutboxMessage(UUID id, UUID sagaID, ZonedDateTime createdAt, ZonedDateTime processedAt, String type, String payload, OutboxStatus outboxStatus, int version, OrderApprovalStatus orderApprovalStatus) {
        super(id, sagaID, createdAt, processedAt, type, payload, outboxStatus, version);
        this.orderApprovalStatus = orderApprovalStatus;
    }
}
