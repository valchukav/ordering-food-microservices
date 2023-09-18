package ru.avalc.ordering.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.saga.SagaStatus;

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
    private SagaStatus sagaStatus;
    private OutboxStatus outboxStatus;
    private int version;
}
