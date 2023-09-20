package ru.avalc.ordering.service.domain.ports.output.message.publisher;

import ru.avalc.ordering.outbox.OutboxMessage;
import ru.avalc.ordering.outbox.OutboxStatus;

import java.util.function.BiConsumer;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

public interface RequestMessagePublisher<T extends OutboxMessage> {

    void publish(T outboxMessage, BiConsumer<T, OutboxStatus> outboxCallback);
}
