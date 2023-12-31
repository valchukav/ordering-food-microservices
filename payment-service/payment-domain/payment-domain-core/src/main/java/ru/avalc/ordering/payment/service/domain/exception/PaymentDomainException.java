package ru.avalc.ordering.payment.service.domain.exception;

import ru.avalc.ordering.system.domain.exception.DomainException;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

public class PaymentDomainException extends DomainException {

    public PaymentDomainException(String message) {
        super(message);
    }

    public PaymentDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
