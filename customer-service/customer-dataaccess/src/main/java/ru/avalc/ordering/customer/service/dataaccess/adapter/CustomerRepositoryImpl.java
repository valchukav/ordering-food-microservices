package ru.avalc.ordering.customer.service.dataaccess.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.avalc.customer.service.domain.entity.Customer;
import ru.avalc.ordering.customer.service.dataaccess.mapper.CustomerDataAccessMapper;
import ru.avalc.ordering.customer.service.dataaccess.repository.CustomerJpaRepository;
import ru.avalc.ordering.customer.service.domain.ports.output.repository.CustomerRepository;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Component
@AllArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;


    @Override
    public Customer createCustomer(Customer customer) {
        return customerDataAccessMapper
                .customerEntityToCustomer(customerJpaRepository.save(customerDataAccessMapper.customerToCustomerEntity(customer)));
    }
}
