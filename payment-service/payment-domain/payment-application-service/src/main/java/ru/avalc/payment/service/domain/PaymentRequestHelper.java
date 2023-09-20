package ru.avalc.payment.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.payment.service.domain.PaymentDomainService;
import ru.avalc.ordering.payment.service.domain.entity.CreditEntry;
import ru.avalc.ordering.payment.service.domain.entity.CreditHistory;
import ru.avalc.ordering.payment.service.domain.entity.Payment;
import ru.avalc.ordering.payment.service.domain.event.PaymentEvent;
import ru.avalc.ordering.payment.service.domain.exception.PaymentNotFoundException;
import ru.avalc.ordering.payment.service.dto.PaymentRequest;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;
import ru.avalc.ordering.system.domain.valueobject.OrderID;
import ru.avalc.ordering.system.domain.valueobject.PaymentStatus;
import ru.avalc.payment.service.domain.exception.PaymentApplicationServiceException;
import ru.avalc.payment.service.domain.mapper.PaymentDataMapper;
import ru.avalc.payment.service.domain.outbox.model.OrderOutboxMessage;
import ru.avalc.payment.service.domain.outbox.scheduler.OrderOutboxHelper;
import ru.avalc.payment.service.domain.ports.output.message.publisher.PaymentResponseMessagePublisher;
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
    private final OrderOutboxHelper orderOutboxHelper;
    private final PaymentResponseMessagePublisher paymentResponseMessagePublisher;

    @Transactional
    public void persistPayment(PaymentRequest paymentRequest) {
        if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.COMPLETED)) {
            log.info("An outbox message with saga id: {} is already saved to database",
                    paymentRequest.getSagaID());
            return;
        }

        log.info("Received payment complete event for order id: {}", paymentRequest.getOrderID());

        Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerID());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerID());
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent =
                paymentDomainService.validateAndInitiatePayment(payment, creditEntry, creditHistories, failureMessages);
        persistDbObjects(payment, creditEntry, creditHistories, failureMessages);

        orderOutboxHelper.saveOrderOutboxMessage(paymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(paymentRequest.getSagaID()));
    }

    @Transactional
    public void persistCancelledPayment(PaymentRequest paymentRequest) {
        if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.CANCELLED)) {
            log.info("An outbox message with saga id: {} is already saved to database",
                    paymentRequest.getSagaID());
            return;
        }

        log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderID());

        Optional<Payment> paymentResponse = paymentRepository.findByOrderId(new OrderID(UUID.fromString(paymentRequest.getOrderID())));

        if (paymentResponse.isEmpty()) {
            log.error("Payment with order id: {} could not be found", paymentRequest.getOrderID());
            throw new PaymentNotFoundException("Payment with order id: " +
                    paymentRequest.getOrderID() + " could not be found");
        }

        Payment payment = paymentResponse.get();
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerID());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerID());
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = paymentDomainService
                .validateAndCancelPayment(payment, creditEntry, creditHistories, failureMessages);
        persistDbObjects(payment, creditEntry, creditHistories, failureMessages);

        orderOutboxHelper.saveOrderOutboxMessage(paymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(paymentRequest.getSagaID()));

    }

    private CreditEntry getCreditEntry(CustomerID customerId) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);
        if (creditEntry.isEmpty()) {
            log.error("Could not find credit entry for customer: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit entry for customer: " +
                    customerId.getValue());
        }
        return creditEntry.get();
    }

    private List<CreditHistory> getCreditHistory(CustomerID customerId) {
        Optional<List<CreditHistory>> creditHistories = creditHistoryRepository.findByCustomerId(customerId);
        if (creditHistories.isEmpty()) {
            log.error("Could not find credit history for customer: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit history for customer: " +
                    customerId.getValue());
        }
        return creditHistories.get();
    }

    private void persistDbObjects(Payment payment,
                                  CreditEntry creditEntry,
                                  List<CreditHistory> creditHistories,
                                  List<String> failureMessages) {
        paymentRepository.save(payment);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }
    }

    private boolean publishIfOutboxMessageProcessedForPayment(PaymentRequest paymentRequest, PaymentStatus paymentStatus) {
        Optional<OrderOutboxMessage> orderOutboxMessage =
                orderOutboxHelper.getCompletedOrderOutboxMessage(UUID.fromString(paymentRequest.getSagaID()), paymentStatus);

        if (orderOutboxMessage.isPresent()) {
            paymentResponseMessagePublisher.publish(orderOutboxMessage.get(), orderOutboxHelper::updateOutboxMessage);
            return true;
        }

        return false;
    }
}
