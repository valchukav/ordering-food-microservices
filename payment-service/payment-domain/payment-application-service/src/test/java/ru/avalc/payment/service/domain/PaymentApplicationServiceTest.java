package ru.avalc.payment.service.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.payment.service.domain.PaymentDomainService;
import ru.avalc.ordering.payment.service.domain.PaymentDomainServiceImpl;
import ru.avalc.ordering.payment.service.domain.entity.CreditEntry;
import ru.avalc.ordering.payment.service.domain.entity.CreditHistory;
import ru.avalc.ordering.payment.service.domain.entity.Payment;
import ru.avalc.ordering.payment.service.domain.event.PaymentEvent;
import ru.avalc.ordering.payment.service.domain.event.PaymentFailedEvent;
import ru.avalc.ordering.payment.service.domain.exception.PaymentNotFoundException;
import ru.avalc.ordering.payment.service.domain.valueobject.CreditEntityID;
import ru.avalc.ordering.payment.service.domain.valueobject.CreditHistoryID;
import ru.avalc.ordering.payment.service.domain.valueobject.PaymentID;
import ru.avalc.ordering.payment.service.domain.valueobject.TransactionType;
import ru.avalc.ordering.payment.service.dto.PaymentRequest;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.OrderID;
import ru.avalc.ordering.system.domain.valueobject.PaymentStatus;
import ru.avalc.ordering.tests.OrderingTest;
import ru.avalc.payment.service.domain.exception.PaymentApplicationServiceException;
import ru.avalc.payment.service.domain.outbox.model.OrderEventPayload;
import ru.avalc.payment.service.domain.outbox.model.OrderOutboxMessage;
import ru.avalc.payment.service.domain.ports.output.repository.CreditEntryRepository;
import ru.avalc.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import ru.avalc.payment.service.domain.ports.output.repository.OrderOutboxRepository;
import ru.avalc.payment.service.domain.ports.output.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.avalc.ordering.saga.order.SagaConstants.ORDER_SAGA_NAME;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@SpringBootTest(classes = PaymentConfigurationTest.class)
public class PaymentApplicationServiceTest extends OrderingTest {

    @Autowired
    private PaymentRequestHelper paymentRequestHelper;

    @Autowired
    private PaymentDomainService paymentDomainService;

    @Autowired
    private CreditEntryRepository creditEntryRepository;

    @Autowired
    private CreditHistoryRepository creditHistoryRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderOutboxRepository orderOutboxRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID creditEntryID = UUID.randomUUID();
    private final String paymentRequestID = UUID.randomUUID().toString();

    private final BigDecimal creditHistory_1_amount = BigDecimal.valueOf(150);
    private final BigDecimal creditHistory_2_amount = BigDecimal.valueOf(50);

    private PaymentRequest paymentRequest;

    private Payment payment;
    private Payment paymentWithHugeAmount;
    private Payment negativePayment;

    private CreditEntry creditEntry;

    private List<CreditHistory> creditHistories;

    private BigDecimal totalCreditEntryAmount;

    @BeforeEach
    public void init() {
        CreditHistory creditHistory_1 = CreditHistory.builder()
                .creditHistoryID(new CreditHistoryID(UUID.randomUUID()))
                .customerID(new CustomerID(CUSTOMER_ID))
                .transactionType(TransactionType.CREDIT)
                .amount(new Money(creditHistory_1_amount))
                .build();

        CreditHistory creditHistory_2 = CreditHistory.builder()
                .creditHistoryID(new CreditHistoryID(UUID.randomUUID()))
                .customerID(new CustomerID(CUSTOMER_ID))
                .transactionType(TransactionType.DEBIT)
                .amount(new Money(creditHistory_2_amount))
                .build();

        creditHistories = new ArrayList<>();
        creditHistories.add(creditHistory_1);
        creditHistories.add(creditHistory_2);

        payment = Payment.builder()
                .paymentID(new PaymentID(PAYMENT_ID))
                .customerID(new CustomerID(CUSTOMER_ID))
                .orderID(new OrderID(ORDER_ID))
                .price(new Money(50))
                .build();

        negativePayment = Payment.builder()
                .customerID(new CustomerID(CUSTOMER_ID))
                .orderID(new OrderID(ORDER_ID))
                .price(new Money(-50))
                .build();

        paymentWithHugeAmount = Payment.builder()
                .customerID(new CustomerID(CUSTOMER_ID))
                .orderID(new OrderID(ORDER_ID))
                .price(new Money(170))
                .build();

        totalCreditEntryAmount = creditHistory_1_amount.subtract(creditHistory_2_amount);

        creditEntry = CreditEntry.builder()
                .creditEntityID(new CreditEntityID(creditEntryID))
                .customerID(new CustomerID(CUSTOMER_ID))
                .totalCreditAmount(new Money(totalCreditEntryAmount))
                .build();

        paymentRequest = PaymentRequest.builder()
                .id(paymentRequestID)
                .sagaID(SAGA_ID.toString())
                .orderID(ORDER_ID.toString())
                .customerID(CUSTOMER_ID.toString())
                .price(payment.getPrice().getAmount())
                .build();

        when(paymentRepository.findByOrderId(any())).thenReturn(Optional.of(payment));
        when(creditEntryRepository.findByCustomerId(new CustomerID(CUSTOMER_ID))).thenReturn(Optional.of(creditEntry));
        when(creditHistoryRepository.findByCustomerId(new CustomerID(CUSTOMER_ID)))
                .thenReturn(Optional.of(new ArrayList<>(List.of(creditHistory_1, creditHistory_2))));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderOutboxRepository.save(any(OrderOutboxMessage.class))).thenReturn(getOrderOutboxMessage());
    }

    @Test
    public void validateAndInitiateWithNegativePaymentAmount() {
        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(negativePayment, creditEntry, creditHistories, new ArrayList<>());

        assertThat(paymentEvent.getPayment().getCustomerID()).isEqualTo(payment.getCustomerID());
        assertThat(paymentEvent.getPayment().getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(paymentEvent.getFailureMessages()).isNotEmpty();
        assertThat(paymentEvent).isInstanceOf(PaymentFailedEvent.class);
    }

    @Test
    public void validateAndInitiateWithAmountGreaterThanTotalCreditAmount() {
        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(paymentWithHugeAmount, creditEntry, creditHistories, new ArrayList<>());

        assertThat(paymentEvent.getPayment().getCustomerID()).isEqualTo(paymentWithHugeAmount.getCustomerID());
        assertThat(paymentEvent.getPayment().getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(paymentEvent.getFailureMessages()).isNotEmpty();
        assertThat(paymentEvent).isInstanceOf(PaymentFailedEvent.class);
    }

    @Test
    public void validateAndInitiateWithInvalidCreditEntry() {
        CreditEntry creditEntry = CreditEntry.builder()
                .creditEntityID(new CreditEntityID(creditEntryID))
                .customerID(new CustomerID(CUSTOMER_ID))
                .totalCreditAmount(new Money(2302))
                .build();

        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(paymentWithHugeAmount, creditEntry, creditHistories, new ArrayList<>());

        assertThat(paymentEvent.getPayment().getCustomerID()).isEqualTo(payment.getCustomerID());
        assertThat(paymentEvent.getPayment().getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(paymentEvent.getFailureMessages()).isNotEmpty();
        assertThat(paymentEvent).isInstanceOf(PaymentFailedEvent.class);
    }

    @Test
    public void validateAndCancelPaymentWithNegativePaymentAmount() {
        PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(negativePayment, creditEntry, creditHistories, new ArrayList<>());

        assertThat(paymentEvent.getPayment().getCustomerID()).isEqualTo(payment.getCustomerID());
        assertThat(paymentEvent.getPayment().getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(paymentEvent.getFailureMessages()).isNotEmpty();
        assertThat(paymentEvent).isInstanceOf(PaymentFailedEvent.class);
    }

    @Test
    public void completePayment() {
        paymentRequestHelper.persistPayment(paymentRequest);
        assertThat(creditEntry.getTotalCreditAmount().getAmount()).isEqualTo(totalCreditEntryAmount.subtract(payment.getPrice().getAmount()));
    }

    @Test
    public void completePaymentWithEmptyCreditEntry() {
        when(creditEntryRepository.findByCustomerId(new CustomerID(CUSTOMER_ID))).thenReturn(Optional.empty());

        assertThrows(PaymentApplicationServiceException.class, () -> paymentRequestHelper.persistPayment(paymentRequest));
    }

    @Test
    public void completePaymentWithEmptyCreditHistory() {
        when(creditHistoryRepository.findByCustomerId(new CustomerID(CUSTOMER_ID))).thenReturn(Optional.empty());

        assertThrows(PaymentApplicationServiceException.class, () -> paymentRequestHelper.persistPayment(paymentRequest));
    }

    @Test
    public void completePaymentWhenMessageWithSagaIDIsAlreadyExists() {
        when(orderOutboxRepository.findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(ORDER_SAGA_NAME, SAGA_ID,
                PaymentStatus.COMPLETED, OutboxStatus.COMPLETED)).thenReturn(Optional.empty());

        paymentRequestHelper.persistPayment(paymentRequest);

        verify(mock(PaymentDomainServiceImpl.class), times(0))
                .validateAndInitiatePayment(any(Payment.class), any(CreditEntry.class), anyList(), anyList());
    }

    @Test
    public void cancelPayment() {
        paymentRequestHelper.persistCancelledPayment(paymentRequest);

        assertThat(creditEntry.getTotalCreditAmount().getAmount()).isEqualTo(totalCreditEntryAmount.add(payment.getPrice().getAmount()));
    }

    @Test
    public void cancelPaymentWithEmptyCreditEntry() {
        when(creditEntryRepository.findByCustomerId(new CustomerID(CUSTOMER_ID))).thenReturn(Optional.empty());

        assertThrows(PaymentApplicationServiceException.class, () -> paymentRequestHelper.persistCancelledPayment(paymentRequest));
    }

    @Test
    public void cancelPaymentWithEmptyCreditHistory() {
        when(creditHistoryRepository.findByCustomerId(new CustomerID(CUSTOMER_ID))).thenReturn(Optional.empty());

        assertThrows(PaymentApplicationServiceException.class, () -> paymentRequestHelper.persistCancelledPayment(paymentRequest));
    }


    @Test
    public void cancelPaymentWithNoPayment() {
        when(paymentRepository.findByOrderId(new OrderID(ORDER_ID))).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> paymentRequestHelper.persistCancelledPayment(paymentRequest));
    }

    @Test
    public void cancelPaymentWhenMessageWithSagaIDIsAlreadyExists() {
        when(orderOutboxRepository.findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(ORDER_SAGA_NAME, SAGA_ID,
                PaymentStatus.COMPLETED, OutboxStatus.COMPLETED)).thenReturn(Optional.empty());

        paymentRequestHelper.persistCancelledPayment(paymentRequest);

        verify(mock(PaymentDomainServiceImpl.class), times(0))
                .validateAndInitiatePayment(any(Payment.class), any(CreditEntry.class), anyList(), anyList());
    }

    private OrderOutboxMessage getOrderOutboxMessage() {
        OrderEventPayload orderEventPayload = OrderEventPayload.builder()
                .paymentID(PAYMENT_ID.toString())
                .orderID(ORDER_ID.toString())
                .customerID(CUSTOMER_ID.toString())
                .price(payment.getPrice().getAmount())
                .createdAt(ZonedDateTime.now())
                .paymentStatus(PaymentStatus.COMPLETED.name())
                .build();

        return OrderOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaID(SAGA_ID)
                .createdAt(ZonedDateTime.now())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderEventPayload))
                .paymentStatus(PaymentStatus.COMPLETED)
                .sagaStatus(SagaStatus.STARTED)
                .outboxStatus(OutboxStatus.STARTED)
                .version(0)
                .build();
    }

    private String createPayload(OrderEventPayload orderEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderEventPayload);
        } catch (JsonProcessingException e) {
            throw new OrderDomainException("Cannot create OrderPaymentEventPayload object!");
        }
    }
}
