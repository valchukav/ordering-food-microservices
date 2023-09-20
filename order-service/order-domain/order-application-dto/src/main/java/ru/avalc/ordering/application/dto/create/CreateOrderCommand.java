package ru.avalc.ordering.application.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@JsonDeserialize(builder = CreateOrderCommand.CreateOrderCommandBuilder.class)
@Builder
@Getter
@AllArgsConstructor
public class CreateOrderCommand {

    @JsonProperty
    @NotNull
    private final UUID customerID;

    @JsonProperty
    @NotNull
    private final UUID restaurantID;

    @JsonProperty
    @NotNull
    private final BigDecimal price;

    @JsonProperty
    @NotNull
    private final List<OrderItem> items;

    @JsonProperty
    @NotNull
    private final OrderAddress address;
}
