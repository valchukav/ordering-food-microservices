package ru.avalc.ordering.customer.service.dataaccess.exception;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

public class CustomerDataAccessException extends RuntimeException {

    public CustomerDataAccessException(String message) {
        super(message);
    }
}
