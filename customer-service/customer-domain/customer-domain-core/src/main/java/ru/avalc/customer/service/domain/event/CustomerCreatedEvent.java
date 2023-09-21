package ru.avalc.customer.service.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.avalc.customer.service.domain.entity.Customer;
import ru.avalc.ordering.system.domain.event.DomainEvent;

import java.time.ZonedDateTime;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@AllArgsConstructor
public class CustomerCreatedEvent implements DomainEvent<Customer> {

    private final Customer customer;
    private final ZonedDateTime createdAt;
}
