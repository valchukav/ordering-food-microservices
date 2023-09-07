package ru.avalc.ordering.system.domain.event.publisher;

import ru.avalc.ordering.system.domain.event.DomainEvent;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

public interface DomainEventPublisher<T extends DomainEvent<?>> {

    void publish(T domainEvent);
}
