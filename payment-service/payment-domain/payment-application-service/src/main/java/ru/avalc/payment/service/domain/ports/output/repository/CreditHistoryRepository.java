package ru.avalc.payment.service.domain.ports.output.repository;

import ru.avalc.ordering.payment.service.domain.entity.CreditHistory;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;

import java.util.List;
import java.util.Optional;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

public interface CreditHistoryRepository {

    CreditHistory save(CreditHistory creditHistory);

    Optional<List<CreditHistory>> findByCustomerId(CustomerID customerID);
}
