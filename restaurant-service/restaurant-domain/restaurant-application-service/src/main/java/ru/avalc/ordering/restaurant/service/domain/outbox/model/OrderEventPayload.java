package ru.avalc.ordering.restaurant.service.domain.outbox.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Alexei Valchuk, 19.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Setter
public class OrderEventPayload {

    @JsonProperty
    private String orderID;

    @JsonProperty
    private String restaurantID;

    @JsonProperty
    private ZonedDateTime createdAt;

    @JsonProperty
    private String orderApprovalStatus;

    @JsonProperty
    private List<String> failureMessages;

    @Builder
    private OrderEventPayload(String orderID, String restaurantID, ZonedDateTime createdAt, String orderApprovalStatus, List<String> failureMessages) {
        this.orderID = orderID;
        this.restaurantID = restaurantID;
        this.createdAt = createdAt;
        this.orderApprovalStatus = orderApprovalStatus;
        this.failureMessages = failureMessages;
    }
}
