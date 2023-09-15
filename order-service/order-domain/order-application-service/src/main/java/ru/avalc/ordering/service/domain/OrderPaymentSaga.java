package ru.avalc.ordering.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.application.dto.message.PaymentResponse;
import ru.avalc.ordering.domain.OrderDomainService;
import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.event.OrderPaidEvent;
import ru.avalc.ordering.saga.SagaStep;
import ru.avalc.ordering.service.domain.ports.output.message.publisher.restaurant.OrderPaidRestaurantRequestMessagePublisher;
import ru.avalc.ordering.system.domain.event.EmptyEvent;

/**
 * @author Alexei Valchuk, 15.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class OrderPaymentSaga implements SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher;
    private final OrderSagaHelper orderSagaHelper;

    @Override
    @Transactional
    public OrderPaidEvent process(PaymentResponse paymentResponse) {
        log.info("Completing payment for order with id: {}", paymentResponse.getOrderID());

        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderID());
        OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order, orderPaidRestaurantRequestMessagePublisher);
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} is paid", order.getId());
        return orderPaidEvent;
    }

    @Override
    @Transactional
    public EmptyEvent rollback(PaymentResponse paymentResponse) {
        log.info("Cancelling order with id {}", paymentResponse.getOrderID());
        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderID());
        orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} is cancelled", order.getId());
        return EmptyEvent.getInstance();
    }
}
