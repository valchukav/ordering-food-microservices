package ru.avalc.payment.service.domain.outbox.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

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

    @Builder
    private OrderEventPayload(String paymentID, String customerID, String orderID, BigDecimal price, ZonedDateTime createdAt, String paymentStatus, List<String> failureMessages) {
        this.paymentID = paymentID;
        this.customerID = customerID;
        this.orderID = orderID;
        this.price = price;
        this.createdAt = createdAt;
        this.paymentStatus = paymentStatus;
        this.failureMessages = failureMessages;
    }
}
