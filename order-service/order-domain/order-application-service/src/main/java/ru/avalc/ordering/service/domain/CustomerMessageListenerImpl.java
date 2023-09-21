package ru.avalc.ordering.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.avalc.ordering.application.dto.message.CustomerModel;
import ru.avalc.ordering.domain.entity.Customer;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.service.domain.mapper.OrderDataMapper;
import ru.avalc.ordering.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
import ru.avalc.ordering.service.domain.ports.output.repository.CustomerRepository;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Service
@AllArgsConstructor
public class CustomerMessageListenerImpl implements CustomerMessageListener {

    private final CustomerRepository customerRepository;
    private final OrderDataMapper orderDataMapper;

    @Override
    public void customerCreated(CustomerModel customerModel) {
        Customer customer = customerRepository.save(orderDataMapper.customerModelToCustomer(customerModel));
        if (customer == null) {
            String message = "Customer could not be created in Order database with id: " + customerModel.getId();
            log.error(message);
            throw new OrderDomainException(message);
        }

        log.info("Customer is created in Order database with id: {}", customerModel.getId());
    }
}
