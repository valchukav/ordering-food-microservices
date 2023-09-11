package ru.avalc.ordering.service.application;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.avalc.ordering.service.domain.ports.input.service.OrderApplicationService;

/**
 * @author Alexei Valchuk, 09.09.2023, email: a.valchukav@gmail.com
 */

@SpringBootApplication(scanBasePackages = "ru.avalc.ordering")
public class OrderConfigurationTest {

    @Bean
    public OrderApplicationService orderApplicationService() {
        return Mockito.mock(OrderApplicationService.class);
    }
}