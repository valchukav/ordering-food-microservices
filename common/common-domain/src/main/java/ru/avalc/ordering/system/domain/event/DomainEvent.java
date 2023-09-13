package ru.avalc.ordering.system.domain.event;

/**
 * @author Alexei Valchuk, 06.09.2023, email: a.valchukav@gmail.com
 */

public interface DomainEvent<T> {

    void fire();
}
