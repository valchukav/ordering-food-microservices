package ru.avalc.ordering.payment.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.system.domain.valueobject.PaymentOrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class PaymentRequest {

    private final String id;
    private final String sagaID;
    private final String orderID;
    private final String customerID;
    private final BigDecimal price;
    private final Instant createdAt;
    @Setter
    private PaymentOrderStatus paymentOrderStatus;

    @Builder
    private PaymentRequest(String id, String sagaID, String orderID, String customerID, BigDecimal price, Instant createdAt, PaymentOrderStatus paymentOrderStatus) {
        this.id = id;
        this.sagaID = sagaID;
        this.orderID = orderID;
        this.customerID = customerID;
        this.price = price;
        this.createdAt = createdAt;
        this.paymentOrderStatus = paymentOrderStatus;
    }
}
