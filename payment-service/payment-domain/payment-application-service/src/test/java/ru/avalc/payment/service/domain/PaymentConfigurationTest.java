package ru.avalc.payment.service.domain;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.avalc.ordering.payment.service.domain.PaymentDomainService;
import ru.avalc.ordering.payment.service.domain.PaymentDomainServiceImpl;
import ru.avalc.payment.service.domain.ports.output.repository.CreditEntryRepository;
import ru.avalc.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import ru.avalc.payment.service.domain.ports.output.repository.PaymentRepository;
import ru.avalc.payment.service.domain.ports.output.repository.message.publisher.PaymentCancelledMessagePublisher;
import ru.avalc.payment.service.domain.ports.output.repository.message.publisher.PaymentCompletedMessagePublisher;
import ru.avalc.payment.service.domain.ports.output.repository.message.publisher.PaymentFailedMessagePublisher;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@SpringBootApplication(scanBasePackages = "ru.avalc.payment")
public class PaymentConfigurationTest {

    @Bean
    public PaymentCancelledMessagePublisher paymentCancelledMessagePublisher() {
        return Mockito.mock(PaymentCancelledMessagePublisher.class);
    }

    @Bean
    public PaymentCompletedMessagePublisher paymentCompletedMessagePublisher() {
        return Mockito.mock(PaymentCompletedMessagePublisher.class);
    }

    @Bean
    public PaymentFailedMessagePublisher paymentFailedMessagePublisher() {
        return Mockito.mock(PaymentFailedMessagePublisher.class);
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
    public PaymentDomainService paymentDomainService() {
        return new PaymentDomainServiceImpl();
    }
}
