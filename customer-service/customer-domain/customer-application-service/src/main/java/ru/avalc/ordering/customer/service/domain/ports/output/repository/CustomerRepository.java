package ru.avalc.ordering.customer.service.domain.ports.output.repository;

import ru.avalc.customer.service.domain.entity.Customer;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

public interface CustomerRepository {

    Customer createCustomer(Customer customer);
}
