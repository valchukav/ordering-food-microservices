package ru.avalc.ordering.payment.service.dataaccess.creditentry.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.payment.service.dataaccess.creditentry.mapper.CreditEntryDataAccessMapper;
import ru.avalc.ordering.payment.service.dataaccess.creditentry.repository.CreditEntryJpaRepository;
import ru.avalc.ordering.payment.service.domain.entity.CreditEntry;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;
import ru.avalc.payment.service.domain.ports.output.repository.CreditEntryRepository;

import java.util.Optional;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Component
@AllArgsConstructor
public class CreditEntryRepositoryImpl implements CreditEntryRepository {

    private final CreditEntryJpaRepository creditEntryJpaRepository;
    private final CreditEntryDataAccessMapper creditEntryDataAccessMapper;

    @Override
    public CreditEntry save(CreditEntry creditEntry) {
        return creditEntryDataAccessMapper
                .creditEntryEntityToCreditEntry(creditEntryJpaRepository
                        .save(creditEntryDataAccessMapper.creditEntryToCreditEntryEntity(creditEntry)));
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(CustomerID customerID) {
        return creditEntryJpaRepository
                .findByCustomerID(customerID.getValue())
                .map(creditEntryDataAccessMapper::creditEntryEntityToCreditEntry);
    }
}
