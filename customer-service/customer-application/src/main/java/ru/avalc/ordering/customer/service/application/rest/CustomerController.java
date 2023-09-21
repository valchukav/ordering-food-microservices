package ru.avalc.ordering.customer.service.application.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.avalc.ordering.customer.service.domain.create.CreateCustomerCommand;
import ru.avalc.ordering.customer.service.domain.create.CreateCustomerResponse;
import ru.avalc.ordering.customer.service.domain.ports.input.service.CustomerApplicationService;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/customers", produces = "application/vnd.api.v1+json")
public class CustomerController {

    private final CustomerApplicationService customerApplicationService;

    @PostMapping
    public ResponseEntity<CreateCustomerResponse> createCustomer(@RequestBody CreateCustomerCommand createCustomerCommand) {
        log.info("Creating customer with username: {}", createCustomerCommand.getUsername());
        CreateCustomerResponse response = customerApplicationService.createCustomer(createCustomerCommand);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
