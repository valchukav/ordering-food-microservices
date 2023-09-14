package ru.avalc.ordering.payment.service.dataaccess.payment.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.payment.service.dataaccess.payment.mapper.PaymentDataAccessMapper;
import ru.avalc.ordering.payment.service.dataaccess.payment.repository.PaymentJpaRepository;
import ru.avalc.ordering.payment.service.domain.entity.Payment;
import ru.avalc.payment.service.domain.ports.output.repository.PaymentRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Component
@AllArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentDataAccessMapper paymentDataAccessMapper;

    @Override
    public Payment save(Payment payment) {
        return paymentDataAccessMapper
                .paymentEntityToPayment(paymentJpaRepository
                        .save(paymentDataAccessMapper.paymentToPaymentEntity(payment)));
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return paymentJpaRepository.findByOrderID(orderId)
                .map(paymentDataAccessMapper::paymentEntityToPayment);
    }
}
