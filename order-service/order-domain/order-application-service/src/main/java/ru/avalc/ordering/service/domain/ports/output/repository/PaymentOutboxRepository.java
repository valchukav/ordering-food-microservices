package ru.avalc.ordering.service.domain.ports.output.repository;

import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

public interface PaymentOutboxRepository extends OutboxRepository<OrderPaymentOutboxMessage> {

}
