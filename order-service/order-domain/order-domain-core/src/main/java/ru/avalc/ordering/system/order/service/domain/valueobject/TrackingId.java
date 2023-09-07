package ru.avalc.ordering.system.order.service.domain.valueobject;

import ru.avalc.ordering.system.domain.valueobject.BaseID;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 06.09.2023, email: a.valchukav@gmail.com
 */

public class TrackingId extends BaseID<UUID> {

    public TrackingId(UUID value) {
        super(value);
    }
}
