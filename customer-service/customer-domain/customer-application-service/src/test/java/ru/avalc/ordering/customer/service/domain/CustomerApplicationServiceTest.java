package ru.avalc.ordering.customer.service.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.avalc.customer.service.domain.entity.Customer;
import ru.avalc.customer.service.domain.exception.CustomerDomainException;
import ru.avalc.ordering.customer.service.domain.create.CreateCustomerCommand;
import ru.avalc.ordering.customer.service.domain.create.CreateCustomerResponse;
import ru.avalc.ordering.customer.service.domain.ports.input.service.CustomerApplicationService;
import ru.avalc.ordering.customer.service.domain.ports.output.repository.CustomerRepository;
import ru.avalc.ordering.tests.OrderingTest;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@SpringBootTest(classes = CustomerConfigurationTest.class)
public class CustomerApplicationServiceTest extends OrderingTest {

    @Autowired
    private CustomerApplicationService customerApplicationService;

    @Autowired
    private CustomerRepository customerRepository;

    private CreateCustomerCommand createCustomerCommand;

    @BeforeEach
    public void init() {
        createCustomerCommand = CreateCustomerCommand.builder()
                .customerID(fullCustomer.getId().getValue())
                .username(fullCustomer.getUsername())
                .firstName(fullCustomer.getFirstName())
                .lastName(fullCustomer.getLastName())
                .build();

        when(customerRepository.createCustomer(any(Customer.class))).thenReturn(fullCustomer);
    }

    @Test
    public void createCustomer() {
        CreateCustomerResponse createCustomerResponse = customerApplicationService.createCustomer(createCustomerCommand);

        assertAll(
                () -> assertThat(createCustomerResponse.getMessage()).isNotBlank(),
                () -> assertThat(createCustomerResponse.getMessage()).contains("success"),
                () -> assertThat(createCustomerResponse.getCustomerID()).isEqualTo(CUSTOMER_ID)
        );
    }

    @Test
    public void createCustomerWhenCustomerDomainExceptionIsThrown() {
        when(customerRepository.createCustomer(any(Customer.class))).thenReturn(null);

        assertThrows(CustomerDomainException.class, () -> customerApplicationService.createCustomer(createCustomerCommand));
    }

    @Test
    public void createCustomerWithInvalidCreateCustomerCommand() {
        createCustomerCommand = CreateCustomerCommand.builder()
                .customerID(null)
                .username(fullCustomer.getUsername())
                .firstName(fullCustomer.getFirstName())
                .lastName(fullCustomer.getLastName())
                .build();

        assertThrows(ConstraintViolationException.class, () -> customerApplicationService.createCustomer(createCustomerCommand));
    }
}
