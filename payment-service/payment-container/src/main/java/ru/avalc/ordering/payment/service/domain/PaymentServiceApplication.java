package ru.avalc.ordering.payment.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@EnableJpaRepositories(basePackages = "ru.avalc.ordering.payment.service.dataaccess")
@EntityScan(basePackages = "ru.avalc.ordering.order.service.dataaccess")
@SpringBootApplication(scanBasePackages = {"ru.avalc.ordering", "ru.avalc.payment"})
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}
