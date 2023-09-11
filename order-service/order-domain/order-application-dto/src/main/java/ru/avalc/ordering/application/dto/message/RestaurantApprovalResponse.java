package ru.avalc.ordering.application.dto.message;

import lombok.Builder;
import lombok.Getter;
import ru.avalc.ordering.system.domain.valueobject.OrderApprovalStatus;

import java.time.Instant;
import java.util.List;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class RestaurantApprovalResponse {

    private final String id;
    private final String sagaID;
    private final String orderID;
    private final String restaurantID;
    private final Instant createdAt;
    private final OrderApprovalStatus orderApprovalStatus;
    private final List<String> failureMessages;

    @Builder
    private RestaurantApprovalResponse(String id, String sagaID, String orderID, String restaurantID, Instant createdAt, OrderApprovalStatus orderApprovalStatus, List<String> failureMessages) {
        this.id = id;
        this.sagaID = sagaID;
        this.orderID = orderID;
        this.restaurantID = restaurantID;
        this.createdAt = createdAt;
        this.orderApprovalStatus = orderApprovalStatus;
        this.failureMessages = failureMessages;
    }
}
