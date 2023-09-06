package ru.avalc.ordering.system.domain.valueobject;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 06.09.2023, email: a.valchukav@gmail.com
 */

public class RestaurantID extends BaseID<UUID>{

    protected RestaurantID(UUID value) {
        super(value);
    }
}
