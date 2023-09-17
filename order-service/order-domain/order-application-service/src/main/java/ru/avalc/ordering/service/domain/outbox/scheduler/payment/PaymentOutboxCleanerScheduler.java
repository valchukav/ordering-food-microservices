package ru.avalc.ordering.service.domain.outbox.scheduler.payment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.outbox.OutboxScheduler;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.outbox.model.OutboxMessage;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 17.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class PaymentOutboxCleanerScheduler implements OutboxScheduler {

    private final PaymentOutboxHelper paymentOutboxHelper;

    @Override
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {

        Optional<List<OrderPaymentOutboxMessage>> paymentOutboxMessagesResponse = paymentOutboxHelper.getPaymentOutboxMessage(
                OutboxStatus.COMPLETED,
                SagaStatus.SUCCEEDED,
                SagaStatus.FAILED,
                SagaStatus.COMPENSATED
        );

        if (paymentOutboxMessagesResponse.isPresent()) {
            List<OrderPaymentOutboxMessage> outboxMessages = paymentOutboxMessagesResponse.get();
            log.info("Received {} OrderPaymentOutboxMessage for cleanup. The payloads: {}",
                    outboxMessages.size(),
                    outboxMessages.stream().map(OutboxMessage::getPayload)
                            .collect(Collectors.joining("\n")));

            paymentOutboxHelper.deletePaymentOutboxMessage(
                    OutboxStatus.COMPLETED,
                    SagaStatus.SUCCEEDED,
                    SagaStatus.FAILED,
                    SagaStatus.COMPENSATED);

            log.info("{} OrderPaymentOutboxMessage deleted", outboxMessages.size());
        }
    }
}
