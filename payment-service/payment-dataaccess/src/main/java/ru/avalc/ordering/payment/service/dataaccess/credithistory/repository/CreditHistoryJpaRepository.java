package ru.avalc.ordering.payment.service.dataaccess.credithistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.avalc.ordering.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Repository
public interface CreditHistoryJpaRepository extends JpaRepository<CreditHistoryEntity, UUID> {

    Optional<List<CreditHistoryEntity>> findByCustomerID(UUID customerId);
}
