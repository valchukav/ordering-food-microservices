package ru.avalc.ordering.restaurant.service.domain.exception;

import ru.avalc.ordering.system.domain.exception.DomainException;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

public class RestaurantApplicationServiceException extends DomainException {

    public RestaurantApplicationServiceException(String message) {
        super(message);
    }

    public RestaurantApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
