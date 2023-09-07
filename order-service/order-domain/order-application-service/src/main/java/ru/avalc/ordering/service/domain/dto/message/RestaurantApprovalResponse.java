package ru.avalc.ordering.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.avalc.ordering.system.domain.valueobject.OrderApprovalStatus;

import java.time.Instant;
import java.util.List;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Builder
@AllArgsConstructor
public class RestaurantApprovalResponse {

    private String id;
    private String sagaID;
    private String orderID;
    private String restaurantID;
    private Instant createdAt;
    private OrderApprovalStatus orderApprovalStatus;
    private final List<String> failureMessages;
}
