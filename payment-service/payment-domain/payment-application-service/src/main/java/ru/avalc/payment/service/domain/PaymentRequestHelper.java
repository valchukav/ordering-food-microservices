package ru.avalc.payment.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.payment.service.domain.PaymentDomainService;
import ru.avalc.ordering.payment.service.domain.entity.CreditEntry;
import ru.avalc.ordering.payment.service.domain.entity.CreditHistory;
import ru.avalc.ordering.payment.service.domain.entity.Payment;
import ru.avalc.ordering.payment.service.domain.event.PaymentEvent;
import ru.avalc.ordering.payment.service.domain.exception.PaymentNotFoundException;
import ru.avalc.ordering.payment.service.dto.PaymentRequest;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;
import ru.avalc.payment.service.domain.exception.PaymentApplicationServiceException;
import ru.avalc.payment.service.domain.mapper.PaymentDataMapper;
import ru.avalc.payment.service.domain.ports.output.repository.CreditEntryRepository;
import ru.avalc.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import ru.avalc.payment.service.domain.ports.output.repository.PaymentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Receive payment complete event for order id: {}", paymentRequest.getOrderID());
        Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        return persistDbObjects(payment, false);
    }

    @Transactional
    public PaymentEvent persistCancelledPayment(PaymentRequest paymentRequest) {
        log.info("Receive payment cancel event for order id: {}", paymentRequest.getOrderID());
        Optional<Payment> optionalPayment = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderID()));
        if (optionalPayment.isEmpty()) {
            String message = "Payment with order id: " + paymentRequest.getOrderID() + " could not be found";
            log.error(message);
            throw new PaymentNotFoundException(message);
        }

        Payment payment = optionalPayment.get();
        return persistDbObjects(payment, true);
    }

    private CreditEntry getCreditEntry(CustomerID customerID) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerID.getValue());
        if (creditEntry.isEmpty()) {
            String message = "Could not find credit entry for customer with id: " + customerID.getValue();
            log.error(message);
            throw new PaymentApplicationServiceException(message);
        } else {
            return creditEntry.get();
        }
    }

    private List<CreditHistory> getCreditHistory(CustomerID customerID) {
        Optional<List<CreditHistory>> creditHistories = creditHistoryRepository.findByCustomerId(customerID.getValue());
        if (creditHistories.isEmpty()) {
            String message = "Could not find credit history for customer with id: " + customerID.getValue();
            log.error(message);
            throw new PaymentApplicationServiceException(message);
        } else {
            return creditHistories.get();
        }
    }

    private PaymentEvent persistDbObjects(Payment payment, boolean isCancelled) {
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerID());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerID());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent;
        if (isCancelled) {
            paymentEvent = paymentDomainService.validateAndCancelPayment(payment, creditEntry, creditHistories, failureMessages);
        } else {
            paymentEvent = paymentDomainService.validateAndInitiatePayment(payment, creditEntry, creditHistories, failureMessages);
        }
        paymentRepository.save(payment);
        if (paymentEvent.getFailureMessages().isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }

        return paymentEvent;
    }
}
