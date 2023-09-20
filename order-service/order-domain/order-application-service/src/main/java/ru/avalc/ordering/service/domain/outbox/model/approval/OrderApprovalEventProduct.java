package ru.avalc.ordering.service.domain.outbox.model.approval;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

@JsonDeserialize(builder = OrderApprovalEventProduct.OrderApprovalEventProductBuilder.class)
@Builder
@AllArgsConstructor
@Getter
@Setter
public class OrderApprovalEventProduct {

    @JsonProperty
    private String id;

    @JsonProperty
    private Integer quantity;
}
