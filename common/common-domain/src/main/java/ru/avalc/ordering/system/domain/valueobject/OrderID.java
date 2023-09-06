package ru.avalc.ordering.system.domain.valueobject;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 06.09.2023, email: a.valchukav@gmail.com
 */

public class OrderID extends BaseID<UUID> {

    public OrderID(UUID value) {
        super(value);
    }
}
