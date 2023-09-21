package ru.avalc.customer.service.domain;

import lombok.extern.slf4j.Slf4j;
import ru.avalc.customer.service.domain.entity.Customer;
import ru.avalc.customer.service.domain.event.CustomerCreatedEvent;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static ru.avalc.ordering.system.domain.DomainConstants.UTC;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
public class CustomerDomainServiceImpl implements CustomerDomainService {

    @Override
    public CustomerCreatedEvent validateAndInitiateCustomer(Customer customer) {
        log.info("Customer with id: {} is initiated", customer.getId().getValue());
        return new CustomerCreatedEvent(customer, ZonedDateTime.now(ZoneId.of(UTC)));
    }
}
