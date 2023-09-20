package ru.avalc.payment.service.domain.outbox.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

@JsonDeserialize(builder = OrderEventPayload.OrderEventPayloadBuilder.class)
@Builder
@AllArgsConstructor
@Getter
@Setter
public class OrderEventPayload {

    @JsonProperty
    private String paymentID;

    @JsonProperty
    private String customerID;

    @JsonProperty
    private String orderID;

    @JsonProperty
    private BigDecimal price;

    @JsonProperty
    private ZonedDateTime createdAt;

    @JsonProperty
    private String paymentStatus;

    @JsonProperty
    private List<String> failureMessages;
}
