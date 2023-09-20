package ru.avalc.ordering.payment.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.system.domain.valueobject.PaymentOrderStatus;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@Builder
@Getter
@AllArgsConstructor
public class PaymentRequest {

    private final String id;
    private final String sagaID;

    @NotNull
    private final String orderID;

    @NotNull
    private final String customerID;

    @NotNull
    private final BigDecimal price;
    private final Instant createdAt;
    @Setter
    private PaymentOrderStatus paymentOrderStatus;
}
