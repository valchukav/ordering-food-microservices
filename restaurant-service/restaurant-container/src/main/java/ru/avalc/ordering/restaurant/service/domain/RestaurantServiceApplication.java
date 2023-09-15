package ru.avalc.ordering.restaurant.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@EnableJpaRepositories(basePackages = {"ru.avalc.ordering.restaurant.service.dataaccess", "ru.avalc.ordering.dataaccess"})
@EntityScan(basePackages = {"ru.avalc.ordering.restaurant.service.dataaccess", "ru.avalc.ordering.dataaccess"})
@SpringBootApplication(scanBasePackages = "ru.avalc.ordering")
public class RestaurantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class, args);
    }
}
