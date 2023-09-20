package ru.avalc.payment.service.domain.ports.output.message.publisher;

import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.payment.service.domain.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

public interface PaymentResponseMessagePublisher {

    void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
