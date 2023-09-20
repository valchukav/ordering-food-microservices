package ru.avalc.ordering.restaurant.service.domain.outbox.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Alexei Valchuk, 19.09.2023, email: a.valchukav@gmail.com
 */

@JsonDeserialize(builder = OrderEventPayload.OrderEventPayloadBuilder.class)
@Builder
@AllArgsConstructor
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
}
