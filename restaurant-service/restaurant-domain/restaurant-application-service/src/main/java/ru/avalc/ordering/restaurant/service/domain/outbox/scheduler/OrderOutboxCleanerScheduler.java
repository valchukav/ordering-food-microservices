package ru.avalc.ordering.restaurant.service.domain.outbox.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.outbox.OutboxScheduler;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.restaurant.service.domain.outbox.model.OrderOutboxMessage;

import java.util.List;
import java.util.Optional;

/**
 * @author Alexei Valchuk, 20.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class OrderOutboxCleanerScheduler implements OutboxScheduler {

    private final OrderOutboxHelper orderOutboxHelper;

    @Transactional
    @Scheduled(cron = "@midnight")
    @Override
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>> outboxMessagesResponse =
                orderOutboxHelper.getOrderOutboxMessage(OutboxStatus.COMPLETED);
        if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
            List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();

            log.info("Received {} OrderOutboxMessage for clean-up!", outboxMessages.size());

            orderOutboxHelper.deleteOrderOutboxMessage(OutboxStatus.COMPLETED);

            log.info("Deleted {} OrderOutboxMessage!", outboxMessages.size());
        }
    }
}
