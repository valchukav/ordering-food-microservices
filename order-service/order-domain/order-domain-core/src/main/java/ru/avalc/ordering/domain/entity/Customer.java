package ru.avalc.ordering.domain.entity;

import lombok.Builder;
import lombok.Getter;
import ru.avalc.ordering.system.domain.entity.AggregateRoot;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class Customer extends AggregateRoot<CustomerID> {

    private final String username;
    private final String firstName;
    private final String lastName;

    @Builder
    private Customer(CustomerID customerID, String username, String firstName, String lastName) {
        super(customerID);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
