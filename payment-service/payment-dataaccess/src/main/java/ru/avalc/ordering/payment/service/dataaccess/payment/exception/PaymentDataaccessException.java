package ru.avalc.ordering.payment.service.dataaccess.payment.exception;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

public class PaymentDataaccessException extends RuntimeException {

    public PaymentDataaccessException(String message) {
        super(message);
    }
}
