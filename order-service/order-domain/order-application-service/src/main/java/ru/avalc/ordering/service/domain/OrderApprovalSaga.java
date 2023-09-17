package ru.avalc.ordering.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.application.dto.message.RestaurantApprovalResponse;
import ru.avalc.ordering.domain.OrderDomainService;
import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.event.OrderCancelledEvent;
import ru.avalc.ordering.saga.SagaStep;

/**
 * @author Alexei Valchuk, 15.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;

    @Override
    @Transactional
    public void process(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Approving order with id: {}", restaurantApprovalResponse.getOrderID());

        Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderID());
        orderDomainService.approveOrder(order);
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} is approved", order.getId());
    }

    @Override
    @Transactional
    public void rollback(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Cancelling order with id: {}", restaurantApprovalResponse.getOrderID());

        Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderID());
        OrderCancelledEvent orderCancelledEvent = orderDomainService
                .cancelOrderPayment(order, restaurantApprovalResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} is cancelled", order.getId());
    }
}
