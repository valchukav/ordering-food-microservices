package ru.avalc.ordering.domain.valueobject;

import ru.avalc.ordering.system.domain.valueobject.BaseID;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 06.09.2023, email: a.valchukav@gmail.com
 */

public class TrackingID extends BaseID<UUID> {

    public TrackingID(UUID value) {
        super(value);
    }
}
