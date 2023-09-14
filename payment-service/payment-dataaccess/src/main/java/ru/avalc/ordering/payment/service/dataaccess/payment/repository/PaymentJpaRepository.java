package ru.avalc.ordering.payment.service.dataaccess.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.avalc.ordering.payment.service.dataaccess.payment.entity.PaymentEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {

    Optional<PaymentEntity> findByOrderID(UUID orderID);
}
