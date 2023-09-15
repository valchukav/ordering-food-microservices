package ru.avalc.ordering.restaurant.service.domain;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.avalc.ordering.restaurant.service.domain.ports.output.OrderApprovalRepository;
import ru.avalc.ordering.restaurant.service.domain.ports.output.OrderApprovedMessagePublisher;
import ru.avalc.ordering.restaurant.service.domain.ports.output.OrderRejectedMessagePublisher;
import ru.avalc.ordering.restaurant.service.domain.ports.output.RestaurantRepository;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@SpringBootApplication(scanBasePackages = "ru.avalc.ordering")
public class RestaurantConfigurationTest {

    @Bean
    public OrderApprovedMessagePublisher orderApprovedMessagePublisher() {
        return Mockito.mock(OrderApprovedMessagePublisher.class);
    }

    @Bean
    public OrderRejectedMessagePublisher orderRejectedMessagePublisher() {
        return Mockito.mock(OrderRejectedMessagePublisher.class);
    }

    @Bean
    public RestaurantRepository restaurantRepository() {
        return Mockito.mock(RestaurantRepository.class);
    }

    @Bean
    public OrderApprovalRepository orderApprovalRepository() {
        return Mockito.mock(OrderApprovalRepository.class);
    }

    @Bean
    public RestaurantDomainService restaurantDomainService() {
        return new RestaurantDomainServiceImpl();
    }
}
