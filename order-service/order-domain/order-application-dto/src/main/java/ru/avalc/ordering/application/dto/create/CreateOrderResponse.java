package ru.avalc.ordering.application.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@JsonDeserialize(builder = CreateOrderResponse.CreateOrderResponseBuilder.class)
@Builder
@Getter
@AllArgsConstructor
public class CreateOrderResponse {

    @JsonProperty
    @NotNull
    private final UUID orderTrackingID;

    @JsonProperty
    @NotNull
    private final OrderStatus orderStatus;

    @JsonProperty
    @NotNull
    private final String message;
}
