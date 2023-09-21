package ru.avalc.ordering.application.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Builder
@Getter
@AllArgsConstructor
public class CustomerModel {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
}
