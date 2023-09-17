package ru.avalc.ordering.service.domain.outbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Setter
@AllArgsConstructor
public abstract class OutboxMessage {

    private UUID id;
    private UUID sagaID;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    private OrderStatus orderStatus;
    private SagaStatus sagaStatus;
    private OutboxStatus outboxStatus;
    private int version;
}
