package ru.avalc.ordering.payment.service.dataaccess.outbox.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.payment.service.dataaccess.outbox.exception.OrderOutboxNotFoundException;
import ru.avalc.ordering.payment.service.dataaccess.outbox.mapper.OrderOutboxDataAccessMapper;
import ru.avalc.ordering.payment.service.dataaccess.outbox.repository.OrderOutboxJpaRepository;
import ru.avalc.ordering.system.domain.valueobject.PaymentStatus;
import ru.avalc.payment.service.domain.outbox.model.OrderOutboxMessage;
import ru.avalc.payment.service.domain.ports.output.repository.OrderOutboxRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 19.09.2023, email: a.valchukav@gmail.com
 */

@Component
@AllArgsConstructor
public class OrderOutboxRepositoryImpl implements OrderOutboxRepository {

    private final OrderOutboxJpaRepository orderOutboxJpaRepository;
    private final OrderOutboxDataAccessMapper orderOutboxDataAccessMapper;

    @Override
    public OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage) {
        return orderOutboxDataAccessMapper
                .orderOutboxEntityToOrderOutboxMessage(orderOutboxJpaRepository
                        .save(orderOutboxDataAccessMapper
                                .orderOutboxMessageToOutboxEntity(orderOutboxMessage)));
    }

    @Override
    public Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String sagaType, OutboxStatus outboxStatus) {
        return Optional.of(orderOutboxJpaRepository.findByTypeAndOutboxStatus(sagaType, outboxStatus)
                .orElseThrow(() -> new OrderOutboxNotFoundException("Approval outbox object " +
                        "cannot be found for saga type " + sagaType))
                .stream()
                .map(orderOutboxDataAccessMapper::orderOutboxEntityToOrderOutboxMessage)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderOutboxMessage> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String sagaType,
                                                                                           UUID sagaID,
                                                                                           PaymentStatus paymentStatus,
                                                                                           OutboxStatus outboxStatus) {
        return orderOutboxJpaRepository.findByTypeAndSagaIDAndPaymentStatusAndOutboxStatus(sagaType, sagaID,
                        paymentStatus, outboxStatus)
                .map(orderOutboxDataAccessMapper::orderOutboxEntityToOrderOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatus(String sagaType, OutboxStatus outboxStatus) {
        orderOutboxJpaRepository.deleteByTypeAndOutboxStatus(sagaType, outboxStatus);
    }
}
