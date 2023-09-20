package ru.avalc.ordering.outbox;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

public interface OutboxScheduler {

    void processOutboxMessage();
}
