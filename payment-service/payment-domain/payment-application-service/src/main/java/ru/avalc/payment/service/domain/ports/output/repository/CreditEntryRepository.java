package ru.avalc.payment.service.domain.ports.output.repository;

import ru.avalc.ordering.payment.service.domain.entity.CreditEntry;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;

import java.util.Optional;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

public interface CreditEntryRepository {

    CreditEntry save(CreditEntry creditEntry);

    Optional<CreditEntry> findByCustomerId(CustomerID customerID);
}
