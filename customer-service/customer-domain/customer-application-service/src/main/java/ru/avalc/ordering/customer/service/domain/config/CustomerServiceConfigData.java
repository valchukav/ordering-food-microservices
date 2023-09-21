package ru.avalc.ordering.customer.service.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "customer-service")
public class CustomerServiceConfigData {

    private String customerTopicName;
}
