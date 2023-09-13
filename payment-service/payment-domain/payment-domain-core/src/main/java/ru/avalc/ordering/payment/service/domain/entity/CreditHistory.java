package ru.avalc.ordering.payment.service.domain.entity;

import lombok.Builder;
import lombok.Getter;
import ru.avalc.ordering.payment.service.domain.valueobject.CreditHistoryID;
import ru.avalc.ordering.payment.service.domain.valueobject.TransactionType;
import ru.avalc.ordering.system.domain.entity.BaseEntity;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;
import ru.avalc.ordering.system.domain.valueobject.Money;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class CreditHistory extends BaseEntity<CreditHistoryID> {

    private final CustomerID customerID;
    private final Money amount;
    private final TransactionType transactionType;

    @Builder
    private CreditHistory(CreditHistoryID creditHistoryID, CustomerID customerID, Money amount, TransactionType transactionType) {
        super(creditHistoryID);
        this.customerID = customerID;
        this.amount = amount;
        this.transactionType = transactionType;
    }
}
