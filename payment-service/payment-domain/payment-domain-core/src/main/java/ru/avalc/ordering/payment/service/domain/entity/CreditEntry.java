package ru.avalc.ordering.payment.service.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.payment.service.domain.valueobject.CreditEntityID;
import ru.avalc.ordering.system.domain.entity.BaseEntity;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;
import ru.avalc.ordering.system.domain.valueobject.Money;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class CreditEntry extends BaseEntity<CreditEntityID> {

    private final CustomerID customerID;
    @Setter
    private Money totalCreditAmount;

    public void addCreditAmount(Money amount) {
        totalCreditAmount.add(amount);
    }

    public void subtractCreditAmount(Money amount) {
        totalCreditAmount.subtract(amount);
    }

    @Builder
    private CreditEntry(CreditEntityID creditEntityID, CustomerID customerID, Money totalCreditAmount) {
        super(creditEntityID);
        this.customerID = customerID;
        this.totalCreditAmount = totalCreditAmount;
    }
}
