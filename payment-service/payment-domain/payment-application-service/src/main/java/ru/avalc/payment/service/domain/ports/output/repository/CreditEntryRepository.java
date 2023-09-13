package ru.avalc.payment.service.domain.ports.output.repository;

import ru.avalc.ordering.payment.service.domain.entity.CreditEntry;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

public interface CreditEntryRepository {

    CreditEntry save(CreditEntry creditEntry);

    Optional<CreditEntry> findByCustomerId(UUID customerID);
}
