package ru.avalc.ordering.order.service.dataaccess.restaurant.exception;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

public class RestaurantDataAccessException extends RuntimeException {

    public RestaurantDataAccessException(String message) {
        super(message);
    }

    public RestaurantDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
