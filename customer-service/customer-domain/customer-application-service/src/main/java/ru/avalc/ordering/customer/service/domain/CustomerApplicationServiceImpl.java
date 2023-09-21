package ru.avalc.ordering.customer.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.avalc.customer.service.domain.event.CustomerCreatedEvent;
import ru.avalc.ordering.customer.service.domain.create.CreateCustomerCommand;
import ru.avalc.ordering.customer.service.domain.create.CreateCustomerResponse;
import ru.avalc.ordering.customer.service.domain.mapper.CustomerDataMapper;
import ru.avalc.ordering.customer.service.domain.ports.input.service.CustomerApplicationService;
import ru.avalc.ordering.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Validated
@Service
@AllArgsConstructor
public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    private final CustomerCreateCommandHandler customerCreateCommandHandler;
    private final CustomerDataMapper customerDataMapper;
    private final CustomerMessagePublisher customerMessagePublisher;

    @Override
    public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomerCommand) {
        CustomerCreatedEvent customerCreatedEvent = customerCreateCommandHandler.createCustomer(createCustomerCommand);
        customerMessagePublisher.publish(customerCreatedEvent);
        return customerDataMapper.customerToCreateCustomerResponse(customerCreatedEvent.getCustomer(), "Customer saved successfully");
    }
}
