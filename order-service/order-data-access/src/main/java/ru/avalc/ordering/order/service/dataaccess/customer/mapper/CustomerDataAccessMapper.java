package ru.avalc.ordering.order.service.dataaccess.customer.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.domain.entity.Customer;
import ru.avalc.ordering.order.service.dataaccess.customer.entity.CustomerEntity;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return Customer.builder()
                .customerID(new CustomerID(customerEntity.getId()))
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
