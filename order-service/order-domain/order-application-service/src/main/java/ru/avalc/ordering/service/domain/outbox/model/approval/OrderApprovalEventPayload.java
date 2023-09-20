package ru.avalc.ordering.service.domain.outbox.model.approval;

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
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

@JsonDeserialize(builder = OrderApprovalEventPayload.OrderApprovalEventPayloadBuilder.class)
@Builder
@AllArgsConstructor
@Getter
@Setter
public class OrderApprovalEventPayload {

    @JsonProperty
    private String orderID;

    @JsonProperty
    private String restaurantID;

    @JsonProperty
    private BigDecimal price;

    @JsonProperty
    private ZonedDateTime createdAt;

    @JsonProperty
    private String restaurantOrderStatus;

    @JsonProperty
    List<OrderApprovalEventProduct> products;
}
