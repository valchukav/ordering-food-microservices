package ru.avalc.ordering.payment.service.domain;

import lombok.extern.slf4j.Slf4j;
import ru.avalc.ordering.payment.service.domain.entity.CreditEntry;
import ru.avalc.ordering.payment.service.domain.entity.CreditHistory;
import ru.avalc.ordering.payment.service.domain.entity.Payment;
import ru.avalc.ordering.payment.service.domain.event.PaymentCancelledEvent;
import ru.avalc.ordering.payment.service.domain.event.PaymentCompletedEvent;
import ru.avalc.ordering.payment.service.domain.event.PaymentEvent;
import ru.avalc.ordering.payment.service.domain.event.PaymentFailedEvent;
import ru.avalc.ordering.payment.service.domain.valueobject.CreditHistoryID;
import ru.avalc.ordering.payment.service.domain.valueobject.TransactionType;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.PaymentStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static ru.avalc.ordering.system.domain.DomainConstants.UTC;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService {

    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment,
                                                   CreditEntry creditEntry,
                                                   List<CreditHistory> creditHistories,
                                                   List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        payment.initPayment();
        validateCreditEntry(payment, creditEntry, failureMessages);
        subtractCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(creditEntry, creditHistories, failureMessages);

        if (failureMessages.isEmpty()) {
            log.info("Payment is initiated for order id {}", payment.getOrderID().getValue());
            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)));
        } else {
            log.info("Payment initiation is failed for order id {}", payment.getOrderID().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages);
        }
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment,
                                                 CreditEntry creditEntry,
                                                 List<CreditHistory> creditHistories,
                                                 List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        addCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);

        if (failureMessages.isEmpty()) {
            log.info("Payment is cancelled for order id {}", payment.getOrderID().getValue());
            payment.updateStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)));
        } else {
            log.info("Payment cancellation is failed for order id {}", payment.getOrderID().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages);
        }
    }

    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
            String message = "Customer with id: " + payment.getCustomerID().getValue() + " does not have enough credit for payment";
            log.error(message);
            failureMessages.add(message);
        }
    }

    private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.subtractCreditAmount(payment.getPrice());
    }

    private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistories, TransactionType transactionType) {
        creditHistories.add(CreditHistory.builder()
                .creditHistoryID(new CreditHistoryID(UUID.randomUUID()))
                .customerID(payment.getCustomerID())
                .amount(payment.getPrice())
                .transactionType(transactionType)
                .build());
    }

    private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {
        Money totalCreditHistory = getTotalHistoryAmount(creditHistories, TransactionType.CREDIT);
        Money totalDebitHistory = getTotalHistoryAmount(creditHistories, TransactionType.DEBIT);

        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            String message = "Customer with id " + creditEntry.getCustomerID().getValue() + " does not have enough credit according to credit history";
            log.error(message);
            failureMessages.add(message);
        }

        if (!creditEntry.getTotalCreditAmount().equals(totalCreditHistory.subtract(totalDebitHistory))) {
            String message = "Credit history total is not equal to current credit for customer with id: " + creditEntry.getCustomerID().getValue();
            log.error(message);
            failureMessages.add(message);
        }
    }

    private Money getTotalHistoryAmount(List<CreditHistory> creditHistories, TransactionType transactionType) {
        return creditHistories.stream()
                .filter(creditHistory -> creditHistory.getTransactionType() == transactionType)
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }

    private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.addCreditAmount(payment.getPrice());
    }
}
