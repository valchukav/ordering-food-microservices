package ru.avalc.ordering.application.dto.create;

import lombok.Builder;
import lombok.Getter;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */


@Getter
public class CreateOrderResponse {

    @NotNull
    private final UUID orderTrackingID;

    @NotNull
    private final OrderStatus orderStatus;

    @NotNull
    private final String message;

    @Builder
    private CreateOrderResponse(UUID orderTrackingID, OrderStatus orderStatus, String message) {
        this.orderTrackingID = orderTrackingID;
        this.orderStatus = orderStatus;
        this.message = message;
    }
}
