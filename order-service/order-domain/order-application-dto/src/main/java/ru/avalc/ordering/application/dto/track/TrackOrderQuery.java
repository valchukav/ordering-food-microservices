package ru.avalc.ordering.application.dto.track;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class TrackOrderQuery {

    @NotNull
    private final UUID orderTrackingID;

    @Builder
    private TrackOrderQuery(UUID orderTrackingID) {
        this.orderTrackingID = orderTrackingID;
    }
}
