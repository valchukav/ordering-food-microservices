package ru.avalc.ordering.order.service.dataaccess.outbox.payment.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class PaymentOutboxDataAccessMapper {

    public PaymentOutboxEntity orderPaymentOutboxMessageToOutboxEntity(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return PaymentOutboxEntity.builder()
                .id(orderPaymentOutboxMessage.getId())
                .sagaID(orderPaymentOutboxMessage.getSagaID())
                .createdAt(orderPaymentOutboxMessage.getCreatedAt())
                .type(orderPaymentOutboxMessage.getType())
                .payload(orderPaymentOutboxMessage.getPayload())
                .orderStatus(orderPaymentOutboxMessage.getOrderStatus())
                .sagaStatus(orderPaymentOutboxMessage.getSagaStatus())
                .outboxStatus(orderPaymentOutboxMessage.getOutboxStatus())
                .version(orderPaymentOutboxMessage.getVersion())
                .build();
    }

    public OrderPaymentOutboxMessage paymentOutboxEntityToOrderPaymentOutboxMessage(PaymentOutboxEntity paymentOutboxEntity) {
        return OrderPaymentOutboxMessage.builder()
                .id(paymentOutboxEntity.getId())
                .sagaID(paymentOutboxEntity.getSagaID())
                .createdAt(paymentOutboxEntity.getCreatedAt())
                .type(paymentOutboxEntity.getType())
                .payload(paymentOutboxEntity.getPayload())
                .orderStatus(paymentOutboxEntity.getOrderStatus())
                .sagaStatus(paymentOutboxEntity.getSagaStatus())
                .outboxStatus(paymentOutboxEntity.getOutboxStatus())
                .version(paymentOutboxEntity.getVersion())
                .build();
    }
}
