package ru.avalc.ordering.order.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@EnableJpaRepositories(basePackages = "ru.avalc.ordering.order.service.dataaccess")
@EntityScan(basePackages = "ru.avalc.ordering.order.service.dataaccess")
@SpringBootApplication(scanBasePackages = "ru.avalc.ordering")
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
