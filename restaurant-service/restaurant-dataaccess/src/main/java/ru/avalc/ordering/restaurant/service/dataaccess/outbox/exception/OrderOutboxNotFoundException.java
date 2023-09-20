package ru.avalc.ordering.restaurant.service.dataaccess.outbox.exception;

/**
 * @author Alexei Valchuk, 19.09.2023, email: a.valchukav@gmail.com
 */

public class OrderOutboxNotFoundException extends RuntimeException {

    public OrderOutboxNotFoundException(String message) {
        super(message);
    }
}
