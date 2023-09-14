package ru.avalc.ordering.restaurant.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.avalc.ordering.restaurant.service.domain.event.OrderApprovalEvent;
import ru.avalc.ordering.restaurant.service.domain.ports.input.RestaurantApprovalRequestMessageListener;
import ru.avalc.ordering.restaurant.service.dto.RestaurantApprovalRequest;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Service
@AllArgsConstructor
public class RestaurantApprovalRequestMessageListenerImpl implements RestaurantApprovalRequestMessageListener {

    private final RestaurantApprovalRequestHelper restaurantApprovalRequestHelper;

    @Override
    public void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest) {
        OrderApprovalEvent orderApprovalEvent = restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest);
        orderApprovalEvent.fire();
    }
}
