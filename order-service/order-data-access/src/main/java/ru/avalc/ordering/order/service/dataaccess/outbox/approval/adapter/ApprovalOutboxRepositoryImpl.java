package ru.avalc.ordering.order.service.dataaccess.outbox.approval.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.order.service.dataaccess.outbox.approval.exception.ApprovalOutboxNotFoundException;
import ru.avalc.ordering.order.service.dataaccess.outbox.approval.mapper.ApprovalOutboxDataAccessMapper;
import ru.avalc.ordering.order.service.dataaccess.outbox.approval.repository.ApprovalOutboxJpaRepository;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import ru.avalc.ordering.service.domain.ports.output.repository.ApprovalOutboxRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

@Component
@AllArgsConstructor
public class ApprovalOutboxRepositoryImpl implements ApprovalOutboxRepository {

    private final ApprovalOutboxJpaRepository approvalOutboxJpaRepository;
    private final ApprovalOutboxDataAccessMapper approvalOutboxDataAccessMapper;

    @Override
    public OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return approvalOutboxDataAccessMapper
                .approvalOutboxEntityToOrderApprovalOutboxMessage(approvalOutboxJpaRepository
                        .save(approvalOutboxDataAccessMapper
                                .orderApprovalOutboxMessageToOutboxEntity(orderApprovalOutboxMessage)));
    }

    @Override
    public Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String sagaType,
                                                                                             OutboxStatus outboxStatus,
                                                                                             SagaStatus... sagaStatus) {
        return Optional.of(approvalOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(sagaType,
                        outboxStatus,
                        Arrays.asList(sagaStatus))
                .orElseThrow(() -> new ApprovalOutboxNotFoundException("Approval outbox object " +
                        "could not be found for saga type " + sagaType))
                .stream()
                .map(approvalOutboxDataAccessMapper::approvalOutboxEntityToOrderApprovalOutboxMessage)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIDAndSagaStatus(String type,
                                                                                 UUID sagaId,
                                                                                 SagaStatus... sagaStatus) {
        return approvalOutboxJpaRepository
                .findByTypeAndSagaIDAndSagaStatusIn(type, sagaId, Arrays.asList(sagaStatus))
                .map(approvalOutboxDataAccessMapper::approvalOutboxEntityToOrderApprovalOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        approvalOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus,
                Arrays.asList(sagaStatus));
    }
}
