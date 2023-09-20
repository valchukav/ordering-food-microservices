package ru.avalc.ordering.restaurant.service.domain.outbox.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.outbox.OutboxScheduler;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import ru.avalc.ordering.restaurant.service.domain.ports.output.publisher.ApprovalResponseMessagePublisher;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 20.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class OrderOutboxScheduler implements OutboxScheduler {

    private final OrderOutboxHelper orderOutboxHelper;
    private final ApprovalResponseMessagePublisher responseMessagePublisher;

    @Transactional
    @Scheduled(fixedRateString = "${restaurant-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${restaurant-service.outbox-scheduler-initial-delay}")

    @Override
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>> outboxMessagesResponse = orderOutboxHelper.getOrderOutboxMessage(OutboxStatus.STARTED);

        if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
            List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();

            log.info("Received {} OrderOutboxMessage with ids {}, sending to message bus", outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage ->
                            outboxMessage.getId().toString()).collect(Collectors.joining(", ")));

            outboxMessages.forEach(orderOutboxMessage ->
                    responseMessagePublisher.publish(orderOutboxMessage, orderOutboxHelper::updateOutboxStatus));

            log.info("{} OrderOutboxMessage sent to message bus", outboxMessages.size());
        }
    }
}
