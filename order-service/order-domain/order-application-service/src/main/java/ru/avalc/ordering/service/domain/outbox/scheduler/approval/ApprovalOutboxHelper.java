package ru.avalc.ordering.service.domain.outbox.scheduler.approval;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import ru.avalc.ordering.service.domain.outbox.scheduler.OutboxHelper;
import ru.avalc.ordering.service.domain.ports.output.repository.ApprovalOutboxRepository;

/**
 * @author Alexei Valchuk, 17.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class ApprovalOutboxHelper extends OutboxHelper<OrderApprovalOutboxMessage> {

    public ApprovalOutboxHelper(ApprovalOutboxRepository outboxRepository) {
        super(outboxRepository);
    }
}
