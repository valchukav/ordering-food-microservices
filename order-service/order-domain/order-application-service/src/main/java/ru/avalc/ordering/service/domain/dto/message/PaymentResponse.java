package ru.avalc.ordering.service.domain.dto.message;

import lombok.AllArgsConstructor;
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
@Builder
@AllArgsConstructor
public class PaymentResponse {

    private String id;
    private String sagaID;
    private String orderID;
    private String paymentID;
    private String customerID;
    private BigDecimal price;
    private Instant createdAt;
    private PaymentStatus paymentStatus;
    private final List<String> failureMessages;
}
