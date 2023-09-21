package ru.avalc.ordering.customer.service.domain.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Builder
@AllArgsConstructor
public class CreateCustomerCommand {

    @NotNull
    private final UUID customerID;

    @NotNull
    private final String username;

    @NotNull
    private final String firstName;

    @NotNull
    private final String lastName;
}
