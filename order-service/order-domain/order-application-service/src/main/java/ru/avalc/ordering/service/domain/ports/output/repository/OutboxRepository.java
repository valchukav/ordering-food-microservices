package ru.avalc.ordering.service.domain.ports.output.repository;

import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.outbox.model.OutboxMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

public interface OutboxRepository<T extends OutboxMessage> {

    T save(T outboxMessage);

    Optional<List<T>> findByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatuses);

    Optional<List<T>> findByTypeAndSagaIDAndSagaStatus(String type, UUID sagaID, SagaStatus... sagaStatuses);

    void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatuses);
}
