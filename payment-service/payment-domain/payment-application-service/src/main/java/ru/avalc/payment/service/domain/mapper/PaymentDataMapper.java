package ru.avalc.payment.service.domain.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.payment.service.domain.entity.Payment;
import ru.avalc.ordering.payment.service.dto.PaymentRequest;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.OrderID;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class PaymentDataMapper {


    public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderID(new OrderID(UUID.fromString(paymentRequest.getOrderID())))
                .customerID(new CustomerID(UUID.fromString(paymentRequest.getCustomerID())))
                .price(new Money(paymentRequest.getPrice()))
                .build();
    }
}
