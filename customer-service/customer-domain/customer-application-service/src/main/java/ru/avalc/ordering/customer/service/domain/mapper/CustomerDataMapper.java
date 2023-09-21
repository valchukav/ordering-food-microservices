package ru.avalc.ordering.customer.service.domain.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.customer.service.domain.entity.Customer;
import ru.avalc.ordering.customer.service.domain.create.CreateCustomerCommand;
import ru.avalc.ordering.customer.service.domain.create.CreateCustomerResponse;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class CustomerDataMapper {

    public Customer createCustomerCommandToCustomer(CreateCustomerCommand createCustomerCommand) {
        return Customer.builder()
                .customerID(new CustomerID(createCustomerCommand.getCustomerID()))
                .username(createCustomerCommand.getUsername())
                .firstName(createCustomerCommand.getFirstName())
                .lastName(createCustomerCommand.getLastName())
                .build();
    }

    public CreateCustomerResponse customerToCreateCustomerResponse(Customer customer, String message) {
        return new CreateCustomerResponse(customer.getId().getValue(), message);
    }
}
