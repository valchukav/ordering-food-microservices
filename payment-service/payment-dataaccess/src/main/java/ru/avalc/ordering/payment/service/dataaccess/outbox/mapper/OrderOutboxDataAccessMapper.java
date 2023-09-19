package ru.avalc.ordering.payment.service.dataaccess.outbox.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import ru.avalc.payment.service.domain.outbox.model.OrderOutboxMessage;

/**
 * @author Alexei Valchuk, 19.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class OrderOutboxDataAccessMapper {

    public OrderOutboxEntity orderOutboxMessageToOutboxEntity(OrderOutboxMessage orderOutboxMessage) {
        return OrderOutboxEntity.builder()
                .id(orderOutboxMessage.getId())
                .sagaID(orderOutboxMessage.getSagaID())
                .createdAt(orderOutboxMessage.getCreatedAt())
                .type(orderOutboxMessage.getType())
                .payload(orderOutboxMessage.getPayload())
                .outboxStatus(orderOutboxMessage.getOutboxStatus())
                .paymentStatus(orderOutboxMessage.getPaymentStatus())
                .version(orderOutboxMessage.getVersion())
                .build();
    }

    public OrderOutboxMessage orderOutboxEntityToOrderOutboxMessage(OrderOutboxEntity paymentOutboxEntity) {
        return OrderOutboxMessage.builder()
                .id(paymentOutboxEntity.getId())
                .sagaID(paymentOutboxEntity.getSagaID())
                .createdAt(paymentOutboxEntity.getCreatedAt())
                .type(paymentOutboxEntity.getType())
                .payload(paymentOutboxEntity.getPayload())
                .outboxStatus(paymentOutboxEntity.getOutboxStatus())
                .paymentStatus(paymentOutboxEntity.getPaymentStatus())
                .version(paymentOutboxEntity.getVersion())
                .build();
    }
}
