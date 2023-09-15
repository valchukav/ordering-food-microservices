package ru.avalc.ordering.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.avalc.ordering.application.dto.message.RestaurantApprovalResponse;
import ru.avalc.ordering.domain.event.OrderCancelledEvent;
import ru.avalc.ordering.service.domain.ports.input.message.listener.restaurant.RestaurantApprovalResponseMessageListener;

import static ru.avalc.ordering.system.domain.DomainConstants.FAILURE_MESSAGE_DELIMITER;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Validated
@Service
@AllArgsConstructor
public class RestaurantApprovalMessageListenerImpl implements RestaurantApprovalResponseMessageListener {

    private final OrderApprovalSaga orderApprovalSaga;

    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {
        orderApprovalSaga.process(restaurantApprovalResponse);
        log.info("Order is approved for order id: {}", restaurantApprovalResponse.getOrderID());
    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {
        OrderCancelledEvent orderCancelledEvent = orderApprovalSaga.rollback(restaurantApprovalResponse);
        log.info("Order with id: {}, is roll backed with failure messages: {}",
                restaurantApprovalResponse.getOrderID(),
                String.join(FAILURE_MESSAGE_DELIMITER, restaurantApprovalResponse.getFailureMessages()));
        orderCancelledEvent.fire();
    }
}
