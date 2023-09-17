package ru.avalc.ordering.service.domain.outbox.scheduler.approval;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.outbox.OutboxScheduler;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.outbox.model.OutboxMessage;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 17.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class RestaurantApprovalOutboxCleanerScheduler implements OutboxScheduler {

    private final ApprovalOutboxHelper approvalOutboxHelper;

    @Override
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {

        Optional<List<OrderApprovalOutboxMessage>> orderApprovalOutboxMessagesResponse = approvalOutboxHelper.getOutboxMessage(
                OutboxStatus.COMPLETED,
                SagaStatus.SUCCEEDED,
                SagaStatus.FAILED,
                SagaStatus.COMPENSATED
        );

        if (orderApprovalOutboxMessagesResponse.isPresent()) {
            List<OrderApprovalOutboxMessage> outboxMessages = orderApprovalOutboxMessagesResponse.get();
            log.info("Received {} OrderApprovalOutboxMessage for cleanup. The payloads: {}",
                    outboxMessages.size(),
                    outboxMessages.stream().map(OutboxMessage::getPayload)
                            .collect(Collectors.joining("\n")));

            approvalOutboxHelper.deleteOutboxMessage(
                    OutboxStatus.COMPLETED,
                    SagaStatus.SUCCEEDED,
                    SagaStatus.FAILED,
                    SagaStatus.COMPENSATED);

            log.info("{} OrderApprovalOutboxMessage deleted", outboxMessages.size());
        }
    }
}
