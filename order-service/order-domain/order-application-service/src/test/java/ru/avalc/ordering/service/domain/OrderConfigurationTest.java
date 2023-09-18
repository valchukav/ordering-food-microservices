package ru.avalc.ordering.service.domain;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.avalc.ordering.domain.OrderDomainService;
import ru.avalc.ordering.domain.OrderDomainServiceImpl;
import ru.avalc.ordering.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import ru.avalc.ordering.service.domain.ports.output.message.publisher.restaurant.RestaurantApprovalRequestMessagePublisher;
import ru.avalc.ordering.service.domain.ports.output.repository.*;

/**
 * @author Alexei Valchuk, 09.09.2023, email: a.valchukav@gmail.com
 */

@SpringBootApplication(scanBasePackages = "ru.avalc.ordering")
public class OrderConfigurationTest {

    @Bean
    public PaymentRequestMessagePublisher paymentRequestMessagePublisher() {
        return Mockito.mock(PaymentRequestMessagePublisher.class);
    }

    @Bean
    public RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher() {
        return Mockito.mock(RestaurantApprovalRequestMessagePublisher.class);
    }

    @Bean
    public OrderRepository orderRepository() {
        return Mockito.mock(OrderRepository.class);
    }

    @Bean
    public CustomerRepository customerRepository() {
        return Mockito.mock(CustomerRepository.class);
    }

    @Bean
    public RestaurantRepository restaurantRepository() {
        return Mockito.mock(RestaurantRepository.class);
    }

    @Bean
    public PaymentOutboxRepository paymentOutboxRepository() {
        return Mockito.mock(PaymentOutboxRepository.class);
    }

    @Bean
    public ApprovalOutboxRepository approvalOutboxRepository() {
        return Mockito.mock(ApprovalOutboxRepository.class);
    }

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }
}