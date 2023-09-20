package ru.avalc.ordering.service.domain.outbox.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.outbox.OutboxMessage;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.ports.output.repository.OutboxRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.avalc.ordering.saga.order.SagaConstants.ORDER_SAGA_NAME;

/**
 * @author Alexei Valchuk, 17.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@AllArgsConstructor
public abstract class OutboxHelper<T extends OutboxMessage> {

    private final OutboxRepository<T> outboxRepository;

    @Transactional(readOnly = true)
    public Optional<List<T>> getOutboxMessage(OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {
        return outboxRepository.findByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }

    @Transactional(readOnly = true)
    public Optional<T> getOutboxMessage(UUID sagaID, SagaStatus... sagaStatuses) {
        return outboxRepository.findByTypeAndSagaIDAndSagaStatus(ORDER_SAGA_NAME, sagaID, sagaStatuses);
    }

    @Transactional
    public void save(T outboxMessage) {
        OutboxMessage response = outboxRepository.save(outboxMessage);
        if (response == null) {
            String message = "Could not save " + outboxMessage.getClass().getSimpleName() + " with outbox id: " + outboxMessage.getId();
            log.error(message);
            throw new OrderDomainException(message);
        }

        log.info("{} is saved with outbox id: {}", outboxMessage.getClass().getSimpleName(), outboxMessage.getId());
    }

    @Transactional
    public void deleteOutboxMessage(OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {
        outboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }
}
