package ru.avalc.ordering.system.domain.event;

/**
 * @author Alexei Valchuk, 15.09.2023, email: a.valchukav@gmail.com
 */

public class EmptyEvent implements DomainEvent<Void> {

    private static final EmptyEvent INSTANCE = new EmptyEvent();

    private EmptyEvent() {
    }

    @Override
    public void fire() {

    }

    public static EmptyEvent getInstance() {
        return INSTANCE;
    }
}
