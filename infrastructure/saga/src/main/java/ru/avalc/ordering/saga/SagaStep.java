package ru.avalc.ordering.saga;

import ru.avalc.ordering.system.domain.event.DomainEvent;

/**
 * @author Alexei Valchuk, 15.09.2023, email: a.valchukav@gmail.com
 */

public interface SagaStep<T, S extends DomainEvent<?>, U extends DomainEvent<?>> {

    S process(T data);

    U rollback(T data);
}
