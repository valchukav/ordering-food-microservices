package ru.avalc.ordering.service.domain.ports.output.repository;

import ru.avalc.ordering.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

public interface CustomerRepository {

    Optional<Customer> findCustomer(UUID customerID);
}
