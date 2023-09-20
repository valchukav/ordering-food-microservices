package ru.avalc.payment.service.domain.ports.output.repository;

import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.system.domain.valueobject.PaymentStatus;
import ru.avalc.payment.service.domain.outbox.model.OrderOutboxMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

public interface OrderOutboxRepository {

    OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage);

    Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus status);

    Optional<OrderOutboxMessage> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String type,
                                                                                    UUID sagaId,
                                                                                    PaymentStatus paymentStatus,
                                                                                    OutboxStatus outboxStatus);

    void deleteByTypeAndOutboxStatus(String type, OutboxStatus status);
}
