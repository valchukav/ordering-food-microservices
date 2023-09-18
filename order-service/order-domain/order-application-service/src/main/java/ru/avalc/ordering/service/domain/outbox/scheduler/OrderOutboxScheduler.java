package ru.avalc.ordering.service.domain.outbox.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.outbox.OutboxMessage;
import ru.avalc.ordering.outbox.OutboxScheduler;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.ports.output.message.publisher.RequestMessagePublisher;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 17.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@AllArgsConstructor
public abstract class OrderOutboxScheduler<T extends OutboxMessage> implements OutboxScheduler {

    private final OutboxHelper<T> outboxHelper;
    private final RequestMessagePublisher<T> requestMessagePublisher;
    private final OutboxStatus outboxStatus;
    private final SagaStatus[] sagaStatuses;

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        Optional<List<T>> outboxMessagesResponse
                = outboxHelper.getOutboxMessage(outboxStatus, sagaStatuses);

        if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
            List<T> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} {} with ids: {}, sending to message bus",
                    outboxMessages.size(),
                    outboxMessages.getClass().getName(),
                    outboxMessages.stream().map(outboxMessage -> outboxMessage.getId().toString()).collect(Collectors.joining(", ")));

            outboxMessages.forEach(outboxMessage -> requestMessagePublisher.publish(outboxMessage, this::updateOutboxStatus));
            log.info("{} {} is sent to message bus", outboxMessages.size(), outboxMessages.getClass().getName());
        }
    }

    private void updateOutboxStatus(T outboxMessage, OutboxStatus outboxStatus) {
        outboxMessage.setOutboxStatus(outboxStatus);
        outboxHelper.save(outboxMessage);
        log.info("{} is updated with outbox status: {}", outboxMessage.getClass().getName(), outboxStatus.name());
    }
}
