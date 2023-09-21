package ru.avalc.ordering.customer.service.domain.create;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@JsonDeserialize(builder = CreateCustomerResponse.CreateCustomerResponseBuilder.class)
@Getter
@Builder
@AllArgsConstructor
public class CreateCustomerResponse {

    @NotNull
    private final UUID customerID;

    @NotNull
    private final String message;
}
