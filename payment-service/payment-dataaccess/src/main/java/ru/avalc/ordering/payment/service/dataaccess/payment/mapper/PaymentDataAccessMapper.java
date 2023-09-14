package ru.avalc.ordering.payment.service.dataaccess.payment.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.payment.service.dataaccess.payment.entity.PaymentEntity;
import ru.avalc.ordering.payment.service.domain.entity.Payment;
import ru.avalc.ordering.payment.service.domain.valueobject.PaymentID;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.OrderID;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class PaymentDataAccessMapper {

    public PaymentEntity paymentToPaymentEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId().getValue())
                .customerID(payment.getCustomerID().getValue())
                .orderID(payment.getOrderID().getValue())
                .price(payment.getPrice().getAmount())
                .status(payment.getPaymentStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public Payment paymentEntityToPayment(PaymentEntity paymentEntity) {
        return Payment.builder()
                .paymentID(new PaymentID(paymentEntity.getId()))
                .customerID(new CustomerID(paymentEntity.getCustomerID()))
                .orderID(new OrderID(paymentEntity.getOrderID()))
                .price(new Money(paymentEntity.getPrice()))
                .createdAt(paymentEntity.getCreatedAt())
                .build();
    }

}
