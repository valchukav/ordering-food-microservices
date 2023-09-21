package ru.avalc.ordering.customer.service.domain;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.avalc.customer.service.domain.CustomerDomainService;
import ru.avalc.customer.service.domain.CustomerDomainServiceImpl;
import ru.avalc.ordering.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher;
import ru.avalc.ordering.customer.service.domain.ports.output.repository.CustomerRepository;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@SpringBootApplication(scanBasePackages = "ru.avalc.ordering")
public class CustomerConfigurationTest {

    @Bean
    public CustomerMessagePublisher customerMessagePublisher() {
        return Mockito.mock(CustomerMessagePublisher.class);
    }

    @Bean
    public CustomerRepository customerRepository() {
        return Mockito.mock(CustomerRepository.class);
    }

    @Bean
    public CustomerDomainService paymentDomainService() {
        return new CustomerDomainServiceImpl();
    }
}
