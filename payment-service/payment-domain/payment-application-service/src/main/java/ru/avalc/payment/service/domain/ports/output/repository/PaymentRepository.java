package ru.avalc.payment.service.domain.ports.output.repository;

import ru.avalc.ordering.payment.service.domain.entity.Payment;
import ru.avalc.ordering.system.domain.valueobject.OrderID;

import java.util.Optional;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByOrderId(OrderID orderID);
}
