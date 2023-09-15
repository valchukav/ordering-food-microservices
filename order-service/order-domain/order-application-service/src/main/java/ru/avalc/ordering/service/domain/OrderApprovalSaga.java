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
import ru.avalc.ordering.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import ru.avalc.ordering.system.domain.event.EmptyEvent;

/**
 * @author Alexei Valchuk, 15.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse, EmptyEvent, OrderCancelledEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher;
    private final OrderSagaHelper orderSagaHelper;

    @Override
    @Transactional
    public EmptyEvent process(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Approving order with id: {}", restaurantApprovalResponse.getOrderID());

        Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderID());
        orderDomainService.approveOrder(order);
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} is approved", order.getId());
        return EmptyEvent.getInstance();
    }

    @Override
    @Transactional
    public OrderCancelledEvent rollback(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Cancelling order with id: {}", restaurantApprovalResponse.getOrderID());

        Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderID());
        OrderCancelledEvent orderCancelledEvent = orderDomainService
                .cancelOrderPayment(order, restaurantApprovalResponse.getFailureMessages(), orderCancelledPaymentRequestMessagePublisher);
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} is cancelled", order.getId());
        return orderCancelledEvent;
    }
}
