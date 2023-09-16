package ru.avalc.ordering.service.domain.outbox.model.approval;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Setter
public class OrderApprovalEventProduct {

    @JsonProperty
    private String id;

    @JsonProperty
    private Integer quantity;

    @Builder
    private OrderApprovalEventProduct(String id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
