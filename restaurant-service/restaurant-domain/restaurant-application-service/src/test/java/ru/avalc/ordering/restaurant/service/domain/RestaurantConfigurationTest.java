package ru.avalc.ordering.restaurant.service.domain;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.avalc.ordering.restaurant.service.domain.ports.output.OrderApprovalRepository;
import ru.avalc.ordering.restaurant.service.domain.ports.output.OrderOutboxRepository;
import ru.avalc.ordering.restaurant.service.domain.ports.output.RestaurantRepository;
import ru.avalc.ordering.restaurant.service.domain.ports.output.publisher.ApprovalResponseMessagePublisher;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@SpringBootApplication(scanBasePackages = "ru.avalc.ordering")
public class RestaurantConfigurationTest {

    @Bean
    public ApprovalResponseMessagePublisher approvalResponseMessagePublisher() {
        return Mockito.mock(ApprovalResponseMessagePublisher.class);
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
    public OrderOutboxRepository orderOutboxRepository() {
        return Mockito.mock(OrderOutboxRepository.class);
    }

    @Bean
    public RestaurantDomainService restaurantDomainService() {
        return new RestaurantDomainServiceImpl();
    }
}
