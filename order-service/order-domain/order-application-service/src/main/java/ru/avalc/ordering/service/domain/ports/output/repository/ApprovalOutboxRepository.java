package ru.avalc.ordering.service.domain.ports.output.repository;

import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

public interface ApprovalOutboxRepository extends OutboxRepository<OrderApprovalOutboxMessage> {

}
