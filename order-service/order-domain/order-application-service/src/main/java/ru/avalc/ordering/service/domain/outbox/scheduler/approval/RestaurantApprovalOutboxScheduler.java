package ru.avalc.ordering.service.domain.outbox.scheduler.approval;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import ru.avalc.ordering.service.domain.outbox.scheduler.OrderOutboxScheduler;
import ru.avalc.ordering.service.domain.ports.output.message.publisher.restaurant.RestaurantApprovalRequestMessagePublisher;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class RestaurantApprovalOutboxScheduler extends OrderOutboxScheduler<OrderApprovalOutboxMessage> {

    public RestaurantApprovalOutboxScheduler(ApprovalOutboxHelper outboxHelper, RestaurantApprovalRequestMessagePublisher requestMessagePublisher) {
        super(outboxHelper, requestMessagePublisher, OutboxStatus.STARTED, new SagaStatus[]{SagaStatus.PROCESSING});
    }
}
