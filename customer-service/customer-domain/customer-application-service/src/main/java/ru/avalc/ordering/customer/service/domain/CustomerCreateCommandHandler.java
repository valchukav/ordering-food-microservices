package ru.avalc.ordering.customer.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.customer.service.domain.CustomerDomainService;
import ru.avalc.customer.service.domain.entity.Customer;
import ru.avalc.customer.service.domain.event.CustomerCreatedEvent;
import ru.avalc.customer.service.domain.exception.CustomerDomainException;
import ru.avalc.ordering.customer.service.domain.create.CreateCustomerCommand;
import ru.avalc.ordering.customer.service.domain.mapper.CustomerDataMapper;
import ru.avalc.ordering.customer.service.domain.ports.output.repository.CustomerRepository;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class CustomerCreateCommandHandler {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;
    private final CustomerDataMapper customerDataMapper;

    @Transactional
    public CustomerCreatedEvent createCustomer(CreateCustomerCommand createCustomerCommand) {
        Customer customer = customerDataMapper.createCustomerCommandToCustomer(createCustomerCommand);
        CustomerCreatedEvent customerCreatedEvent = customerDomainService.validateAndInitiateCustomer(customer);
        Customer savedCustomer = customerRepository.createCustomer(customer);

        if (savedCustomer == null) {
            String message = "Could not save customer with id: " + createCustomerCommand.getCustomerID();
            log.error(message);
            throw new CustomerDomainException(message);
        }

        log.info("Returning CustomerCreatedEvent with customer id: {}", createCustomerCommand.getCustomerID());
        return customerCreatedEvent;
    }
}
