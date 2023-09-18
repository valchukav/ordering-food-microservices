package ru.avalc.ordering.order.service.dataaccess.outbox.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.avalc.ordering.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

@Repository
public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutboxEntity, UUID> {

    Optional<List<PaymentOutboxEntity>> findByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                                                 OutboxStatus outboxStatus,
                                                                                 List<SagaStatus> sagaStatus);

    Optional<PaymentOutboxEntity> findByTypeAndSagaIDAndSagaStatusIn(String type,
                                                                     UUID sagaId,
                                                                     List<SagaStatus> sagaStatus);

    void deleteByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                    OutboxStatus outboxStatus,
                                                    List<SagaStatus> sagaStatus);
}
