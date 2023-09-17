package ru.avalc.ordering.service.domain.outbox.scheduler.approval;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import ru.avalc.ordering.service.domain.outbox.scheduler.OutboxHelper;
import ru.avalc.ordering.service.domain.ports.output.repository.ApprovalOutboxRepository;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;

import java.util.UUID;

import static ru.avalc.ordering.saga.order.SagaConstants.ORDER_SAGA_NAME;

/**
 * @author Alexei Valchuk, 17.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
public class ApprovalOutboxHelper extends OutboxHelper<OrderApprovalOutboxMessage> {

    private final ObjectMapper objectMapper;

    public ApprovalOutboxHelper(ApprovalOutboxRepository outboxRepository, ObjectMapper objectMapper) {
        super(outboxRepository);
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void saveApprovalOutboxMessage(OrderApprovalEventPayload orderApprovalEventPayload,
                                          OrderStatus orderStatus,
                                          SagaStatus sagaStatus,
                                          OutboxStatus outboxStatus,
                                          UUID sagaID) {
        save(OrderApprovalOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaID(sagaID)
                .createdAt(orderApprovalEventPayload.getCreatedAt())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderApprovalEventPayload))
                .orderStatus(orderStatus)
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    private String createPayload(OrderApprovalEventPayload orderApprovalEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderApprovalEventPayload);
        } catch (JsonProcessingException e) {
            String message = "Could not create OrderApprovalEventPayload for order id: " + orderApprovalEventPayload.getOrderID();
            log.error(message);
            throw new OrderDomainException(message, e);
        }
    }
}
