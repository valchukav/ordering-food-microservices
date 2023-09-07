package ru.avalc.ordering.service.domain.dto.track;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderQuery {

    @NotNull
    private final UUID orderTrackingID;
}
