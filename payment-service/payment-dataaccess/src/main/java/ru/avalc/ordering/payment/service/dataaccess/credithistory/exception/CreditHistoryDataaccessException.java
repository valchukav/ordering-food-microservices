package ru.avalc.ordering.payment.service.dataaccess.credithistory.exception;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

public class CreditHistoryDataaccessException extends RuntimeException {

    public CreditHistoryDataaccessException(String message) {
        super(message);
    }
}
