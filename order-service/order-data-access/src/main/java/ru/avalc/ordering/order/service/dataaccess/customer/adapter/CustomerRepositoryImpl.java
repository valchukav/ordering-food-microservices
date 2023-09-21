package ru.avalc.ordering.order.service.dataaccess.customer.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.domain.entity.Customer;
import ru.avalc.ordering.order.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import ru.avalc.ordering.order.service.dataaccess.customer.repository.CustomerJpaRepository;
import ru.avalc.ordering.service.domain.ports.output.repository.CustomerRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Component
@AllArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    @Override
    public Optional<Customer> findCustomer(UUID customerID) {
        return customerJpaRepository.findById(customerID).map(customerDataAccessMapper::customerEntityToCustomer);
    }

    @Override
    public Customer save(Customer customer) {
        return customerDataAccessMapper
                .customerEntityToCustomer(customerJpaRepository.save(customerDataAccessMapper.customerToCustomerEntity(customer)));
    }
}
