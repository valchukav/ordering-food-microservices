package ru.avalc.ordering.application.dto.message;

import lombok.Builder;
import lombok.Getter;
import ru.avalc.ordering.system.domain.valueobject.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class PaymentResponse {

    private final String id;
    private final String sagaID;
    private final String orderID;
    private final String paymentID;
    private final String customerID;
    private final BigDecimal price;
    private final Instant createdAt;
    private final PaymentStatus paymentStatus;
    private final List<String> failureMessages;

    @Builder
    private PaymentResponse(String id, String sagaID, String orderID, String paymentID, String customerID, BigDecimal price, Instant createdAt, PaymentStatus paymentStatus, List<String> failureMessages) {
        this.id = id;
        this.sagaID = sagaID;
        this.orderID = orderID;
        this.paymentID = paymentID;
        this.customerID = customerID;
        this.price = price;
        this.createdAt = createdAt;
        this.paymentStatus = paymentStatus;
        this.failureMessages = failureMessages;
    }
}
