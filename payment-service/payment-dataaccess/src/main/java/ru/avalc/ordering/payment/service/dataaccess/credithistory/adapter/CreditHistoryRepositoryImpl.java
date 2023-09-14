package ru.avalc.ordering.payment.service.dataaccess.credithistory.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;
import ru.avalc.ordering.payment.service.dataaccess.credithistory.mapper.CreditHistoryDataAccessMapper;
import ru.avalc.ordering.payment.service.dataaccess.credithistory.repository.CreditHistoryJpaRepository;
import ru.avalc.ordering.payment.service.domain.entity.CreditHistory;
import ru.avalc.payment.service.domain.ports.output.repository.CreditHistoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Component
@AllArgsConstructor
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository creditHistoryJpaRepository;
    private final CreditHistoryDataAccessMapper creditHistoryDataAccessMapper;

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        return creditHistoryDataAccessMapper.creditHistoryEntityToCreditHistory(creditHistoryJpaRepository
                .save(creditHistoryDataAccessMapper.creditHistoryToCreditHistoryEntity(creditHistory)));
    }

    @Override
    public Optional<List<CreditHistory>> findByCustomerId(UUID customerID) {
        Optional<List<CreditHistoryEntity>> creditHistory =
                creditHistoryJpaRepository.findByCustomerID(customerID);
        return creditHistory
                .map(creditHistoryList ->
                        creditHistoryList.stream()
                                .map(creditHistoryDataAccessMapper::creditHistoryEntityToCreditHistory)
                                .collect(Collectors.toList()));
    }
}
