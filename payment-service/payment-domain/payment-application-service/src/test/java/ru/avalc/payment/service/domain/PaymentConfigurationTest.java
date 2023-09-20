package ru.avalc.payment.service.domain;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.avalc.ordering.payment.service.domain.PaymentDomainService;
import ru.avalc.ordering.payment.service.domain.PaymentDomainServiceImpl;
import ru.avalc.payment.service.domain.ports.output.message.publisher.PaymentResponseMessagePublisher;
import ru.avalc.payment.service.domain.ports.output.repository.CreditEntryRepository;
import ru.avalc.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import ru.avalc.payment.service.domain.ports.output.repository.OrderOutboxRepository;
import ru.avalc.payment.service.domain.ports.output.repository.PaymentRepository;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@SpringBootApplication(scanBasePackages = "ru.avalc.payment")
public class PaymentConfigurationTest {

    @Bean
    public PaymentResponseMessagePublisher paymentResponseMessagePublisher() {
        return Mockito.mock(PaymentResponseMessagePublisher.class);
    }

    @Bean
    public CreditEntryRepository creditEntryRepository() {
        return Mockito.mock(CreditEntryRepository.class);
    }

    @Bean
    public CreditHistoryRepository creditHistoryRepository() {
        return Mockito.mock(CreditHistoryRepository.class);
    }

    @Bean
    public PaymentRepository paymentRepository() {
        return Mockito.mock(PaymentRepository.class);
    }

    @Bean
    public OrderOutboxRepository orderOutboxRepository() {
        return Mockito.mock(OrderOutboxRepository.class);
    }

    @Bean
    public PaymentDomainService paymentDomainService() {
        return new PaymentDomainServiceImpl();
    }
}
