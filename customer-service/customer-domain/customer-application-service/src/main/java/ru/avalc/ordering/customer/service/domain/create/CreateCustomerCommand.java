package ru.avalc.ordering.customer.service.domain.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@JsonDeserialize(builder = CreateCustomerCommand.CreateCustomerCommandBuilder.class)
@Getter
@Builder
@AllArgsConstructor
public class CreateCustomerCommand {

    @JsonProperty
    @NotNull
    private final UUID customerID;

    @JsonProperty
    @NotNull
    private final String username;

    @JsonProperty
    @NotNull
    private final String firstName;

    @JsonProperty
    @NotNull
    private final String lastName;
}
