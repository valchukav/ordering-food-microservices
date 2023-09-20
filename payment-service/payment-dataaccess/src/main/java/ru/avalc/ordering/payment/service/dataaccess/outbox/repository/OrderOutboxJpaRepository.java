package ru.avalc.ordering.payment.service.dataaccess.outbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import ru.avalc.ordering.system.domain.valueobject.PaymentStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 19.09.2023, email: a.valchukav@gmail.com
 */

@Repository
public interface OrderOutboxJpaRepository extends JpaRepository<OrderOutboxEntity, UUID> {

    Optional<List<OrderOutboxEntity>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

    Optional<OrderOutboxEntity> findByTypeAndSagaIDAndPaymentStatusAndOutboxStatus(String type,
                                                                                   UUID sagaID,
                                                                                   PaymentStatus paymentStatus,
                                                                                   OutboxStatus outboxStatus);

    void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);
}
