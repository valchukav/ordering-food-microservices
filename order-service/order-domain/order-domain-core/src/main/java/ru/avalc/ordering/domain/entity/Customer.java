package ru.avalc.ordering.domain.entity;

import lombok.Builder;
import ru.avalc.ordering.system.domain.entity.AggregateRoot;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

public class Customer extends AggregateRoot<CustomerID> {

    @Builder
    private Customer(CustomerID customerID) {
        super(customerID);
    }
}
