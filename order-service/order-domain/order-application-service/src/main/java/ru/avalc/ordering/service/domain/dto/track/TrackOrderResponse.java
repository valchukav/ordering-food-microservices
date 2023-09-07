package ru.avalc.ordering.service.domain.dto.track;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderResponse {

    @NotNull
    private final UUID orderTrackingID;

    @NotNull
    private final OrderStatus orderStatus;

    private final List<String> failureMessages;
}
