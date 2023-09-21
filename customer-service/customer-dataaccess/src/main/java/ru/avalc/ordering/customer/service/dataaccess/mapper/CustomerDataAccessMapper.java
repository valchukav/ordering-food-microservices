package ru.avalc.ordering.customer.service.dataaccess.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.customer.service.domain.entity.Customer;
import ru.avalc.ordering.customer.service.dataaccess.entity.CustomerEntity;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return Customer.builder()
                .customerID(new CustomerID(customerEntity.getId()))
                .username(customerEntity.getUsername())
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .build();
    }

    public CustomerEntity customerToCustomerEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId().getValue())
                .username(customer.getUsername())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
    }
}
