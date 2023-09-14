package ru.avalc.ordering.payment.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Configuration
public class BeanConfiguration {

    @Bean
    public PaymentDomainService orderDomainService() {
        return new PaymentDomainServiceImpl();
    }
}
