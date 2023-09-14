package ru.avalc.ordering.payment.service.dataaccess.creditentry.exception;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

public class CreditEntryDataaccessException extends RuntimeException {

    public CreditEntryDataaccessException(String message) {
        super(message);
    }
}
