package ru.avalc.ordering.payment.service.domain.valueobject;

import ru.avalc.ordering.system.domain.valueobject.BaseID;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

public class CreditEntityID extends BaseID<UUID> {

    public CreditEntityID(UUID value) {
        super(value);
    }
}
