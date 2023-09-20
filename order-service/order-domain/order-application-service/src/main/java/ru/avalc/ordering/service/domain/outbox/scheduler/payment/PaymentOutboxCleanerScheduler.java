package ru.avalc.ordering.service.domain.outbox.scheduler.payment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.outbox.OutboxScheduler;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 17.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@AllArgsConstructor
@Component
public class PaymentOutboxCleanerScheduler implements OutboxScheduler {

    private final PaymentOutboxHelper paymentOutboxHelper;

    @Override
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        Optional<List<OrderPaymentOutboxMessage>> outboxMessagesResponse =
                paymentOutboxHelper.getOutboxMessage(
                        OutboxStatus.COMPLETED,
                        SagaStatus.SUCCEEDED,
                        SagaStatus.FAILED,
                        SagaStatus.COMPENSATED);

        if (outboxMessagesResponse.isPresent()) {
            List<OrderPaymentOutboxMessage> outboxMessages = outboxMessagesResponse.get();

            log.info("Received {} OrderPaymentOutboxMessage for clean-up. The payloads: {}",
                    outboxMessages.size(),
                    outboxMessages.stream().map(OrderPaymentOutboxMessage::getPayload)
                            .collect(Collectors.joining("\n")));

            paymentOutboxHelper.deleteOutboxMessage(
                    OutboxStatus.COMPLETED,
                    SagaStatus.SUCCEEDED,
                    SagaStatus.FAILED,
                    SagaStatus.COMPENSATED);

            log.info("{} OrderPaymentOutboxMessage deleted", outboxMessages.size());
        }
    }
}
