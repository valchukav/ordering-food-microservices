package ru.avalc.ordering.domain.valueobject;

import ru.avalc.ordering.system.domain.valueobject.BaseID;

/**
 * @author Alexei Valchuk, 06.09.2023, email: a.valchukav@gmail.com
 */

public class OrderItemID extends BaseID<Long> {

    public OrderItemID(Long value) {
        super(value);
    }
}
