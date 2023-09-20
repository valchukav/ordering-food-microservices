package ru.avalc.ordering.service.domain.outbox.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

@JsonDeserialize(builder = OrderPaymentEventPayload.OrderPaymentEventPayloadBuilder.class)
@Builder
@AllArgsConstructor
@Getter
@Setter
public class OrderPaymentEventPayload {

    @JsonProperty
    private String orderID;

    @JsonProperty
    private String customerID;

    @JsonProperty
    private BigDecimal price;

    @JsonProperty
    private ZonedDateTime createdAt;

    @JsonProperty
    private String paymentOrderStatus;
}
