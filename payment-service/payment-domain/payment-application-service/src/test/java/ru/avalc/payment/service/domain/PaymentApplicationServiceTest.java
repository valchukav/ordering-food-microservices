package ru.avalc.payment.service.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.avalc.ordering.payment.service.domain.entity.CreditEntry;
import ru.avalc.ordering.payment.service.domain.entity.CreditHistory;
import ru.avalc.ordering.payment.service.domain.entity.Payment;
import ru.avalc.ordering.payment.service.domain.event.PaymentCancelledEvent;
import ru.avalc.ordering.payment.service.domain.event.PaymentCompletedEvent;
import ru.avalc.ordering.payment.service.domain.event.PaymentEvent;
import ru.avalc.ordering.payment.service.domain.event.PaymentFailedEvent;
import ru.avalc.ordering.payment.service.domain.valueobject.CreditEntityID;
import ru.avalc.ordering.payment.service.domain.valueobject.CreditHistoryID;
import ru.avalc.ordering.payment.service.domain.valueobject.TransactionType;
import ru.avalc.ordering.payment.service.dto.PaymentRequest;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.OrderID;
import ru.avalc.ordering.system.domain.valueobject.PaymentStatus;
import ru.avalc.payment.service.domain.exception.PaymentApplicationServiceException;
import ru.avalc.payment.service.domain.ports.output.repository.CreditEntryRepository;
import ru.avalc.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import ru.avalc.payment.service.domain.ports.output.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@SpringBootTest(classes = PaymentConfigurationTest.class)
public class PaymentApplicationServiceTest {

    @Autowired
    private PaymentRequestHelper paymentRequestHelper;

    @Autowired
    private CreditEntryRepository creditEntryRepository;

    @Autowired
    private CreditHistoryRepository creditHistoryRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private final UUID creditEntryID = UUID.randomUUID();
    private final String paymentRequestID = UUID.randomUUID().toString();
    private final UUID orderID = UUID.randomUUID();
    private final UUID customerID = UUID.randomUUID();

    private final BigDecimal creditHistory_1_amount = BigDecimal.valueOf(150);
    private final BigDecimal creditHistory_2_amount = BigDecimal.valueOf(50);

    private PaymentRequest paymentRequest;
    private PaymentRequest paymentRequestWithHugeAmount;
    private PaymentRequest paymentRequestWithNegativeAmount;

    private Payment payment;
    private Payment paymentWithHugeAmount;
    private Payment negativePayment;

    private CreditEntry creditEntry;
    private BigDecimal totalCreditEntryAmount;

    @BeforeEach
    public void init() {
        CreditHistory creditHistory_1 = CreditHistory.builder()
                .creditHistoryID(new CreditHistoryID(UUID.randomUUID()))
                .customerID(new CustomerID(customerID))
                .transactionType(TransactionType.CREDIT)
                .amount(new Money(creditHistory_1_amount))
                .build();

        CreditHistory creditHistory_2 = CreditHistory.builder()
                .creditHistoryID(new CreditHistoryID(UUID.randomUUID()))
                .customerID(new CustomerID(customerID))
                .transactionType(TransactionType.DEBIT)
                .amount(new Money(creditHistory_2_amount))
                .build();

        payment = Payment.builder()
                .customerID(new CustomerID(customerID))
                .orderID(new OrderID(orderID))
                .price(new Money(50))
                .build();

        negativePayment = Payment.builder()
                .customerID(new CustomerID(customerID))
                .orderID(new OrderID(orderID))
                .price(new Money(-50))
                .build();

        paymentWithHugeAmount = Payment.builder()
                .customerID(new CustomerID(customerID))
                .orderID(new OrderID(orderID))
                .price(new Money(170))
                .build();

        totalCreditEntryAmount = creditHistory_1_amount.subtract(creditHistory_2_amount);

        creditEntry = CreditEntry.builder()
                .creditEntityID(new CreditEntityID(creditEntryID))
                .customerID(new CustomerID(customerID))
                .totalCreditAmount(new Money(totalCreditEntryAmount))
                .build();

        paymentRequest = PaymentRequest.builder()
                .id(paymentRequestID)
                .orderID(orderID.toString())
                .customerID(customerID.toString())
                .price(payment.getPrice().getAmount())
                .build();

        paymentRequestWithHugeAmount = PaymentRequest.builder()
                .id(paymentRequestID)
                .orderID(orderID.toString())
                .customerID(customerID.toString())
                .price(paymentWithHugeAmount.getPrice().getAmount())
                .build();

        paymentRequestWithNegativeAmount = PaymentRequest.builder()
                .id(paymentRequestID)
                .orderID(orderID.toString())
                .customerID(customerID.toString())
                .price(negativePayment.getPrice().getAmount())
                .build();

        when(paymentRepository.findByOrderId(any())).thenReturn(Optional.of(payment));
        when(creditEntryRepository.findByCustomerId(new CustomerID(customerID))).thenReturn(Optional.of(creditEntry));
        when(creditHistoryRepository.findByCustomerId(new CustomerID(customerID)))
                .thenReturn(Optional.of(new ArrayList<>(List.of(creditHistory_1, creditHistory_2))));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
    }

    @Test
    public void completePayment() {
        PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequest);

        assertThat(paymentEvent.getPayment().getCustomerID()).isEqualTo(payment.getCustomerID());
        assertThat(paymentEvent.getPayment().getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(paymentEvent.getFailureMessages()).isEmpty();
        assertThat(creditEntry.getTotalCreditAmount().getAmount()).isEqualTo(totalCreditEntryAmount.subtract(payment.getPrice().getAmount()));
        assertThat(paymentEvent).isInstanceOf(PaymentCompletedEvent.class);
    }

    @Test
    public void completePaymentWithNegativePaymentAmount() {
        when(paymentRepository.findByOrderId(any())).thenReturn(Optional.of(negativePayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(negativePayment);

        PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequestWithNegativeAmount);

        assertThat(paymentEvent.getPayment().getCustomerID()).isEqualTo(payment.getCustomerID());
        assertThat(paymentEvent.getPayment().getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(paymentEvent.getFailureMessages()).isNotEmpty();
        assertThat(paymentEvent).isInstanceOf(PaymentFailedEvent.class);
    }

    @Test
    public void completePaymentWithAmountGreaterThanTotalCreditAmount() {
        when(paymentRepository.findByOrderId(any())).thenReturn(Optional.of(paymentWithHugeAmount));
        when(paymentRepository.save(any(Payment.class))).thenReturn(paymentWithHugeAmount);

        PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequestWithHugeAmount);

        assertThat(paymentEvent.getPayment().getCustomerID()).isEqualTo(paymentWithHugeAmount.getCustomerID());
        assertThat(paymentEvent.getPayment().getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(paymentEvent.getFailureMessages()).isNotEmpty();
        assertThat(paymentEvent).isInstanceOf(PaymentFailedEvent.class);
    }

    @Test
    public void completePaymentWithInvalidCreditEntry() {
        CreditEntry creditEntry = CreditEntry.builder()
                .creditEntityID(new CreditEntityID(creditEntryID))
                .customerID(new CustomerID(customerID))
                .totalCreditAmount(new Money(2302))
                .build();

        when(creditEntryRepository.findByCustomerId(new CustomerID(customerID))).thenReturn(Optional.of(creditEntry));

        PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequest);

        assertThat(paymentEvent.getPayment().getCustomerID()).isEqualTo(payment.getCustomerID());
        assertThat(paymentEvent.getPayment().getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(paymentEvent.getFailureMessages()).isNotEmpty();
        assertThat(paymentEvent).isInstanceOf(PaymentFailedEvent.class);
    }

    @Test
    public void completePaymentWithEmptyCreditEntry() {
        when(creditEntryRepository.findByCustomerId(new CustomerID(customerID))).thenReturn(Optional.empty());

        assertThrows(PaymentApplicationServiceException.class, () -> paymentRequestHelper.persistPayment(paymentRequest));
    }

    @Test
    public void completePaymentWithEmptyCreditHistory() {
        when(creditHistoryRepository.findByCustomerId(new CustomerID(customerID))).thenReturn(Optional.empty());

        assertThrows(PaymentApplicationServiceException.class, () -> paymentRequestHelper.persistPayment(paymentRequest));
    }

    @Test
    public void cancelPayment() {
        PaymentEvent paymentEvent = paymentRequestHelper.persistCancelledPayment(paymentRequest);

        assertThat(paymentEvent.getPayment().getCustomerID()).isEqualTo(payment.getCustomerID());
        assertThat(paymentEvent.getPayment().getPaymentStatus()).isEqualTo(PaymentStatus.CANCELLED);
        assertThat(paymentEvent.getFailureMessages()).isEmpty();
        assertThat(creditEntry.getTotalCreditAmount().getAmount()).isEqualTo(totalCreditEntryAmount.add(payment.getPrice().getAmount()));
        assertThat(paymentEvent).isInstanceOf(PaymentCancelledEvent.class);
    }

    @Test
    public void cancelPaymentWithNegativePaymentAmount() {
        when(paymentRepository.findByOrderId(any())).thenReturn(Optional.of(negativePayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(negativePayment);

        PaymentEvent paymentEvent = paymentRequestHelper.persistCancelledPayment(paymentRequestWithNegativeAmount);

        assertThat(paymentEvent.getPayment().getCustomerID()).isEqualTo(payment.getCustomerID());
        assertThat(paymentEvent.getPayment().getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(paymentEvent.getFailureMessages()).isNotEmpty();
        assertThat(paymentEvent).isInstanceOf(PaymentFailedEvent.class);
    }

    @Test
    public void cancelPaymentWithEmptyCreditEntry() {
        when(creditEntryRepository.findByCustomerId(new CustomerID(customerID))).thenReturn(Optional.empty());

        assertThrows(PaymentApplicationServiceException.class, () -> paymentRequestHelper.persistCancelledPayment(paymentRequest));
    }

    @Test
    public void cancelPaymentWithEmptyCreditHistory() {
        when(creditHistoryRepository.findByCustomerId(new CustomerID(customerID))).thenReturn(Optional.empty());

        assertThrows(PaymentApplicationServiceException.class, () -> paymentRequestHelper.persistCancelledPayment(paymentRequest));
    }
}
