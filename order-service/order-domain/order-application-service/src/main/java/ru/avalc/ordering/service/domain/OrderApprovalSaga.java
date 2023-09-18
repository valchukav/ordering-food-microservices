package ru.avalc.ordering.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.application.dto.message.RestaurantApprovalResponse;
import ru.avalc.ordering.domain.OrderDomainService;
import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.event.OrderCancelledEvent;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.saga.SagaStep;
import ru.avalc.ordering.service.domain.mapper.OrderDataMapper;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import ru.avalc.ordering.service.domain.outbox.scheduler.approval.ApprovalOutboxHelper;
import ru.avalc.ordering.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static ru.avalc.ordering.system.domain.DomainConstants.UTC;

/**
 * @author Alexei Valchuk, 15.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final OrderDataMapper orderDataMapper;

    @Override
    @Transactional
    public void process(RestaurantApprovalResponse restaurantApprovalResponse) {
        Optional<OrderApprovalOutboxMessage> outboxMessageResponse = approvalOutboxHelper.getOutboxMessage(
                UUID.fromString(restaurantApprovalResponse.getSagaID()),
                SagaStatus.PROCESSING
        );

        if (outboxMessageResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already processed", restaurantApprovalResponse.getSagaID());
            return;
        }

        OrderApprovalOutboxMessage outboxMessage = outboxMessageResponse.get();
        Order order = approveOrder(restaurantApprovalResponse);

        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(order.getOrderStatus());
        approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(outboxMessage, order.getOrderStatus(), sagaStatus));

        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(restaurantApprovalResponse.getSagaID(), order.getOrderStatus(), sagaStatus));

        log.info("Order with id: {} is approved", order.getId());
    }

    @Override
    @Transactional
    public void rollback(RestaurantApprovalResponse restaurantApprovalResponse) {
        Optional<OrderApprovalOutboxMessage> outboxMessageResponse = approvalOutboxHelper.getOutboxMessage(
                UUID.fromString(restaurantApprovalResponse.getSagaID()),
                SagaStatus.PROCESSING
        );

        if (outboxMessageResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already roll backed", restaurantApprovalResponse.getSagaID());
            return;
        }

        OrderApprovalOutboxMessage outboxMessage = outboxMessageResponse.get();
        OrderCancelledEvent orderCancelledEvent = rollbackOrder(restaurantApprovalResponse);

        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(orderCancelledEvent.getOrder().getOrderStatus());
        approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(outboxMessage, orderCancelledEvent.getOrder().getOrderStatus(), sagaStatus));

        paymentOutboxHelper.savePaymentOutboxMessage(
                orderDataMapper.orderCancelledEventToOrderPaymentEventPayload(orderCancelledEvent),
                orderCancelledEvent.getOrder().getOrderStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(restaurantApprovalResponse.getRestaurantID())
        );

        log.info("Order with id: {} is cancelled", orderCancelledEvent.getOrder().getId());
    }

    private Order approveOrder(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Approving order with id: {}", restaurantApprovalResponse.getOrderID());
        Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderID());
        orderDomainService.approveOrder(order);
        orderSagaHelper.saveOrder(order);
        return order;
    }

    private OrderCancelledEvent rollbackOrder(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Cancelling order with id: {}", restaurantApprovalResponse.getOrderID());
        Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderID());
        OrderCancelledEvent orderCancelledEvent = orderDomainService
                .cancelOrderPayment(order, restaurantApprovalResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        return orderCancelledEvent;
    }

    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(OrderApprovalOutboxMessage outboxMessage,
                                                                       OrderStatus orderStatus, SagaStatus sagaStatus) {
        outboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        outboxMessage.setOrderStatus(orderStatus);
        outboxMessage.setSagaStatus(sagaStatus);
        return outboxMessage;
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(String sagaID, OrderStatus orderStatus, SagaStatus sagaStatus) {
        Optional<OrderPaymentOutboxMessage> outboxMessageResponse
                = paymentOutboxHelper.getOutboxMessage(UUID.fromString(sagaID), SagaStatus.PROCESSING);

        if (outboxMessageResponse.isEmpty()) {
            throw new OrderDomainException("Payment outbox message could not be found un " + SagaStatus.PROCESSING.name() + " state");
        }

        OrderPaymentOutboxMessage outboxMessage = outboxMessageResponse.get();
        outboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        outboxMessage.setOrderStatus(orderStatus);
        outboxMessage.setSagaStatus(sagaStatus);
        return outboxMessage;
    }
}
