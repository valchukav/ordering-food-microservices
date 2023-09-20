package ru.avalc.ordering.restaurant.service.domain.ports.output.publisher;

import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.restaurant.service.domain.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

/**
 * @author Alexei Valchuk, 19.09.2023, email: a.valchukav@gmail.com
 */

public interface ApprovalResponseMessagePublisher {

    void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
