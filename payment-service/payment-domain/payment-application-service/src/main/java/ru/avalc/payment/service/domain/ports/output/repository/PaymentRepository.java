package ru.avalc.payment.service.domain.ports.output.repository;

import ru.avalc.ordering.payment.service.domain.entity.Payment;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByOrderId(UUID orderID);
}
