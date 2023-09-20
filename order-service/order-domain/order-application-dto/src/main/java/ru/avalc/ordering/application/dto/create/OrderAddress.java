package ru.avalc.ordering.application.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@JsonDeserialize(builder = OrderAddress.OrderAddressBuilder.class)
@Builder
@Getter
@AllArgsConstructor
public class OrderAddress {

    @JsonProperty
    @NotNull
    @Max(value = 50)
    private final String street;

    @JsonProperty
    @NotNull
    @Max(value = 10)
    private final String postalCode;

    @JsonProperty
    @NotNull
    @Max(value = 50)
    private final String city;
}
