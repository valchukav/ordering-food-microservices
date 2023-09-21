package ru.avalc.ordering.customer.service.application;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.avalc.ordering.customer.service.domain.ports.input.service.CustomerApplicationService;

/**
 * @author Alexei Valchuk, 09.09.2023, email: a.valchukav@gmail.com
 */

@SpringBootApplication(scanBasePackages = "ru.avalc.ordering")
public class CustomerControllerConfigurationTest {

    @Bean
    public CustomerApplicationService customerApplicationService() {
        return Mockito.mock(CustomerApplicationService.class);
    }
}