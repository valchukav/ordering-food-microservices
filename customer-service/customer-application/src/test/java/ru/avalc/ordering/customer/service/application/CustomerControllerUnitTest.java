package ru.avalc.ordering.customer.service.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.avalc.customer.service.domain.exception.CustomerDomainException;
import ru.avalc.ordering.customer.service.application.rest.CustomerController;
import ru.avalc.ordering.customer.service.domain.create.CreateCustomerCommand;
import ru.avalc.ordering.customer.service.domain.create.CreateCustomerResponse;
import ru.avalc.ordering.customer.service.domain.ports.input.service.CustomerApplicationService;
import ru.avalc.ordering.tests.OrderingTest;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Alexei Valchuk, 11.09.2023, email: a.valchukav@gmail.com
 */

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerUnitTest extends OrderingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerApplicationService customerApplicationService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateCustomerCommand createCustomerCommand;

    @BeforeEach
    public void init() {
        createCustomerCommand = CreateCustomerCommand.builder()
                .customerID(fullCustomer.getId().getValue())
                .username(fullCustomer.getUsername())
                .firstName(fullCustomer.getFirstName())
                .lastName(fullCustomer.getLastName())
                .build();

        CreateCustomerResponse createCustomerResponse = CreateCustomerResponse.builder()
                .customerID(CUSTOMER_ID)
                .message("Customer saves successfully")
                .build();

        when(customerApplicationService.createCustomer(any(CreateCustomerCommand.class))).thenReturn(createCustomerResponse);
    }

    @Test
    public void createCustomer() throws Exception {
        ResultActions response = mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerCommand)));

        response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerID", is(CUSTOMER_ID.toString())));
    }

    @Test
    public void createCustomerWhenCustomerDomainExceptionIsThrown() throws Exception {
        when(customerApplicationService.createCustomer(any(CreateCustomerCommand.class))).thenThrow(CustomerDomainException.class);

        ResultActions response = mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerCommand)));

        response
                .andExpect(status().isBadRequest());
    }
}
