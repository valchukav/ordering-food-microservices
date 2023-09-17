package ru.avalc.ordering.service.domain.outbox.scheduler.approval;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import ru.avalc.ordering.service.domain.outbox.scheduler.OrderOutboxCleanerScheduler;

/**
 * @author Alexei Valchuk, 17.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class RestaurantApprovalOutboxCleanerScheduler extends OrderOutboxCleanerScheduler<OrderApprovalOutboxMessage> {

    public RestaurantApprovalOutboxCleanerScheduler(ApprovalOutboxHelper outboxHelper) {
        super(outboxHelper);
    }
}
