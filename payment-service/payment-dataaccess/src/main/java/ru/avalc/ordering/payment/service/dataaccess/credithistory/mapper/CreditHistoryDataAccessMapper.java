package ru.avalc.ordering.payment.service.dataaccess.credithistory.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;
import ru.avalc.ordering.payment.service.domain.entity.CreditHistory;
import ru.avalc.ordering.payment.service.domain.valueobject.CreditHistoryID;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;
import ru.avalc.ordering.system.domain.valueobject.Money;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class CreditHistoryDataAccessMapper {

    public CreditHistory creditHistoryEntityToCreditHistory(CreditHistoryEntity creditHistoryEntity) {
        return CreditHistory.builder()
                .creditHistoryID(new CreditHistoryID(creditHistoryEntity.getId()))
                .customerID(new CustomerID(creditHistoryEntity.getCustomerID()))
                .amount(new Money(creditHistoryEntity.getAmount()))
                .transactionType(creditHistoryEntity.getType())
                .build();
    }

    public CreditHistoryEntity creditHistoryToCreditHistoryEntity(CreditHistory creditHistory) {
        return CreditHistoryEntity.builder()
                .id(creditHistory.getId().getValue())
                .customerID(creditHistory.getCustomerID().getValue())
                .amount(creditHistory.getAmount().getAmount())
                .type(creditHistory.getTransactionType())
                .build();
    }
}
