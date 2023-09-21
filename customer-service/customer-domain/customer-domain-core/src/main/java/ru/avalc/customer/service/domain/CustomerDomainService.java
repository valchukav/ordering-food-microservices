package ru.avalc.customer.service.domain;

import ru.avalc.customer.service.domain.entity.Customer;
import ru.avalc.customer.service.domain.event.CustomerCreatedEvent;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

public interface CustomerDomainService {

    CustomerCreatedEvent validateAndInitiateCustomer(Customer customer);
}
