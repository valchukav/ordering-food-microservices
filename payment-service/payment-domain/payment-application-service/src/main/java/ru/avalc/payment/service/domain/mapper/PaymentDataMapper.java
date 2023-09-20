package ru.avalc.payment.service.domain.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.payment.service.domain.entity.Payment;
import ru.avalc.ordering.payment.service.domain.event.PaymentEvent;
import ru.avalc.ordering.payment.service.dto.PaymentRequest;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.OrderID;
import ru.avalc.payment.service.domain.outbox.model.OrderEventPayload;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class PaymentDataMapper {

    public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderID(new OrderID(UUID.fromString(paymentRequest.getOrderID())))
                .customerID(new CustomerID(UUID.fromString(paymentRequest.getCustomerID())))
                .price(new Money(paymentRequest.getPrice()))
                .build();
    }

    public OrderEventPayload paymentEventToOrderEventPayload(PaymentEvent paymentEvent) {
        return OrderEventPayload.builder()
                .paymentID(paymentEvent.getPayment().getId().getValue().toString())
                .customerID(paymentEvent.getPayment().getCustomerID().getValue().toString())
                .orderID(paymentEvent.getPayment().getOrderID().getValue().toString())
                .price(paymentEvent.getPayment().getPrice().getAmount())
                .createdAt(paymentEvent.getCreatedAt())
                .paymentStatus(paymentEvent.getPayment().getPaymentStatus().name())
                .failureMessages(paymentEvent.getFailureMessages())
                .build();
    }
}
