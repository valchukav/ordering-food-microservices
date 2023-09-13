package ru.avalc.ordering.payment.service.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.payment.service.domain.valueobject.PaymentID;
import ru.avalc.ordering.system.domain.entity.AggregateRoot;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.OrderID;
import ru.avalc.ordering.system.domain.valueobject.PaymentStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static ru.avalc.ordering.system.domain.DomainConstants.UTC;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class Payment extends AggregateRoot<PaymentID> {

    private final OrderID orderID;
    private final CustomerID customerID;
    private final Money price;

    @Setter
    private PaymentStatus paymentStatus;
    @Setter
    private ZonedDateTime createdAt;

    public void initPayment() {
        setId(new PaymentID(UUID.randomUUID()));
        createdAt = ZonedDateTime.now(ZoneId.of(UTC));
    }

    public void validatePayment(List<String> failureMessages) {
        if (price == null || !price.isGreaterThanZero()) {
            failureMessages.add("Total price must be greater than zero: " + price);
        }
    }

    public void updateStatus(PaymentStatus status) {
        paymentStatus = status;
    }

    @Builder
    private Payment(PaymentID paymentID, OrderID orderID, CustomerID customerID, Money price, PaymentStatus paymentStatus, ZonedDateTime createdAt) {
        super(paymentID);
        this.orderID = orderID;
        this.customerID = customerID;
        this.price = price;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
    }
}