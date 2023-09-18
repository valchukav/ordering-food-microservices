package ru.avalc.ordering.service.domain.outbox.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import ru.avalc.ordering.outbox.OutboxMessage;
import ru.avalc.ordering.outbox.OutboxScheduler;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 17.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@AllArgsConstructor
public abstract class OrderOutboxCleanerScheduler<T extends OutboxMessage> implements OutboxScheduler {

    private final OutboxHelper<T> outboxHelper;

    @Override
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {

        Optional<List<T>> outboxMessagesResponse = outboxHelper.getOutboxMessage(
                OutboxStatus.COMPLETED,
                SagaStatus.SUCCEEDED,
                SagaStatus.FAILED,
                SagaStatus.COMPENSATED
        );

        if (outboxMessagesResponse.isPresent()) {
            List<T> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} {} for cleanup. The payloads: {}",
                    outboxMessages.size(),
                    outboxMessages.getClass().getName(),
                    outboxMessages.stream().map(OutboxMessage::getPayload)
                            .collect(Collectors.joining("\n")));

            outboxHelper.deleteOutboxMessage(
                    OutboxStatus.COMPLETED,
                    SagaStatus.SUCCEEDED,
                    SagaStatus.FAILED,
                    SagaStatus.COMPENSATED);

            log.info("{} {} deleted", outboxMessages.size(), outboxMessages.getClass().getName());
        }
    }
}
