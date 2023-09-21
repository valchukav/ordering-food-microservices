package ru.avalc.customer.service.domain.exception;

import ru.avalc.ordering.system.domain.exception.DomainException;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

public class CustomerDomainException extends DomainException {

    public CustomerDomainException(String message) {
        super(message);
    }

    public CustomerDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
