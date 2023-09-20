package ru.avalc.ordering.order.service.dataaccess.outbox.payment.exception;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

public class PaymentOutboxNotFoundException extends RuntimeException {

    public PaymentOutboxNotFoundException(String message) {
        super(message);
    }
}
