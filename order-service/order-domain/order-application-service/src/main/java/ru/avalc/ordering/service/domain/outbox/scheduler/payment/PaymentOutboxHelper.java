package ru.avalc.ordering.service.domain.outbox.scheduler.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import ru.avalc.ordering.service.domain.outbox.scheduler.OutboxHelper;
import ru.avalc.ordering.service.domain.ports.output.repository.PaymentOutboxRepository;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;

import java.util.UUID;

import static ru.avalc.ordering.saga.order.SagaConstants.ORDER_SAGA_NAME;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
public class PaymentOutboxHelper extends OutboxHelper<OrderPaymentOutboxMessage> {

    private final ObjectMapper objectMapper;

    public PaymentOutboxHelper(PaymentOutboxRepository outboxRepository, ObjectMapper objectMapper) {
        super(outboxRepository);
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void savePaymentOutboxMessage(OrderPaymentEventPayload orderPaymentEventPayload,
                                         OrderStatus orderStatus,
                                         SagaStatus sagaStatus,
                                         OutboxStatus outboxStatus,
                                         UUID sagaID) {
        save(OrderPaymentOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaID(sagaID)
                .createdAt(orderPaymentEventPayload.getCreatedAt())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderPaymentEventPayload))
                .orderStatus(orderStatus)
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    private String createPayload(OrderPaymentEventPayload orderPaymentEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderPaymentEventPayload);
        } catch (JsonProcessingException e) {
            String message = "Could not create OrderPaymentEventPayload for order id: " + orderPaymentEventPayload.getOrderID();
            log.error(message);
            throw new OrderDomainException(message, e);
        }
    }
}
