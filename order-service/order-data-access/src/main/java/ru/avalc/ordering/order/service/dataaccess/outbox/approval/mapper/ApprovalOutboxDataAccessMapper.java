package ru.avalc.ordering.order.service.dataaccess.outbox.approval.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.order.service.dataaccess.outbox.approval.entity.ApprovalOutboxEntity;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class ApprovalOutboxDataAccessMapper {

    public ApprovalOutboxEntity orderApprovalOutboxMessageToOutboxEntity(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return ApprovalOutboxEntity.builder()
                .id(orderApprovalOutboxMessage.getId())
                .sagaID(orderApprovalOutboxMessage.getSagaID())
                .createdAt(orderApprovalOutboxMessage.getCreatedAt())
                .type(orderApprovalOutboxMessage.getType())
                .payload(orderApprovalOutboxMessage.getPayload())
                .orderStatus(orderApprovalOutboxMessage.getOrderStatus())
                .sagaStatus(orderApprovalOutboxMessage.getSagaStatus())
                .outboxStatus(orderApprovalOutboxMessage.getOutboxStatus())
                .version(orderApprovalOutboxMessage.getVersion())
                .build();
    }

    public OrderApprovalOutboxMessage approvalOutboxEntityToOrderApprovalOutboxMessage(ApprovalOutboxEntity approvalOutboxEntity) {
        return OrderApprovalOutboxMessage.builder()
                .id(approvalOutboxEntity.getId())
                .sagaID(approvalOutboxEntity.getSagaID())
                .createdAt(approvalOutboxEntity.getCreatedAt())
                .type(approvalOutboxEntity.getType())
                .payload(approvalOutboxEntity.getPayload())
                .orderStatus(approvalOutboxEntity.getOrderStatus())
                .sagaStatus(approvalOutboxEntity.getSagaStatus())
                .outboxStatus(approvalOutboxEntity.getOutboxStatus())
                .version(approvalOutboxEntity.getVersion())
                .build();
    }
}
