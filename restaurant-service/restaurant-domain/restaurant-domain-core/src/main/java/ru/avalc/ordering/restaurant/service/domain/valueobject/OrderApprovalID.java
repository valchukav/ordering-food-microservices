package ru.avalc.ordering.restaurant.service.domain.valueobject;

import ru.avalc.ordering.system.domain.valueobject.BaseID;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

public class OrderApprovalID extends BaseID<UUID> {

    public OrderApprovalID(UUID value) {
        super(value);
    }
}
