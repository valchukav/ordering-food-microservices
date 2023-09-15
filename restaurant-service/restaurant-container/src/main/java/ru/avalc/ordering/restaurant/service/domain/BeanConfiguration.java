package ru.avalc.ordering.restaurant.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Configuration
public class BeanConfiguration {

    @Bean
    public RestaurantDomainService orderDomainService() {
        return new RestaurantDomainServiceImpl();
    }
}
