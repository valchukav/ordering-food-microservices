package ru.avalc.ordering.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Builder
@AllArgsConstructor
public class OrderAddress {

    @NotNull
    @Max(value = 50)
    private final String street;

    @NotNull
    @Max(value = 10)
    private final String postalCode;

    @NotNull
    @Max(value = 50)
    private final String city;
}
