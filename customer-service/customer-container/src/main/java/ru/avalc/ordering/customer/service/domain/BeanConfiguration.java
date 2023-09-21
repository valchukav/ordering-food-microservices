package ru.avalc.ordering.customer.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.avalc.customer.service.domain.CustomerDomainService;
import ru.avalc.customer.service.domain.CustomerDomainServiceImpl;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Configuration
public class BeanConfiguration {

    @Bean
    public CustomerDomainService customerDomainService() {
        return new CustomerDomainServiceImpl();
    }
}
