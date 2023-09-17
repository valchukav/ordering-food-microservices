package ru.avalc.ordering.service.domain.outbox.scheduler.approval;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.outbox.OutboxScheduler;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import ru.avalc.ordering.service.domain.ports.output.message.publisher.restaurant.RestaurantApprovalRequestMessagePublisher;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class RestaurantApprovalOutboxScheduler implements OutboxScheduler {

    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher;

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        Optional<List<OrderApprovalOutboxMessage>> approvalOutboxMessagesResponse
                = approvalOutboxHelper.getOutboxMessage(OutboxStatus.STARTED, SagaStatus.PROCESSING);

        if (approvalOutboxMessagesResponse.isPresent() && approvalOutboxMessagesResponse.get().size() > 0) {
            List<OrderApprovalOutboxMessage> outboxMessages = approvalOutboxMessagesResponse.get();
            log.info("Received {} OrderPaymentOutboxMessage with ids: {}, sending to message bus",
                    outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage -> outboxMessage.getId().toString()).collect(Collectors.joining(", ")));

            outboxMessages.forEach(outboxMessage -> restaurantApprovalRequestMessagePublisher.publish(outboxMessage, this::updateOutboxStatus));
            log.info("{} OrderPaymentOutboxMessage is sent to message bus", outboxMessages.size());
        }
    }

    private void updateOutboxStatus(OrderApprovalOutboxMessage orderApprovalOutboxMessage, OutboxStatus outboxStatus) {
        orderApprovalOutboxMessage.setOutboxStatus(outboxStatus);
        approvalOutboxHelper.save(orderApprovalOutboxMessage);
        log.info("OrderPaymentOutboxMessage is updated with outbox status: {}", outboxStatus.name());
    }
}
