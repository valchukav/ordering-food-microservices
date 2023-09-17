package ru.avalc.ordering.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.application.dto.message.PaymentResponse;
import ru.avalc.ordering.domain.OrderDomainService;
import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.event.OrderPaidEvent;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.saga.SagaStep;
import ru.avalc.ordering.service.domain.mapper.OrderDataMapper;
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
public class OrderPaymentSaga implements SagaStep<PaymentResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final OrderDataMapper orderDataMapper;

    @Override
    @Transactional
    public void process(PaymentResponse paymentResponse) {
        Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessageResponse = paymentOutboxHelper.getOutboxMessage(
                UUID.fromString(paymentResponse.getSagaID()),
                SagaStatus.STARTED
        );

        if (orderPaymentOutboxMessageResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already processed", paymentResponse.getSagaID());
            return;
        }

        OrderPaymentOutboxMessage outboxMessage = orderPaymentOutboxMessageResponse.get();

        log.info("Completing payment for order with id: {}", paymentResponse.getOrderID());

        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderID());
        OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order);
        orderSagaHelper.saveOrder(order);

        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(orderPaidEvent.getOrder().getOrderStatus());
        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(outboxMessage, orderPaidEvent.getOrder().getOrderStatus(), sagaStatus));

        approvalOutboxHelper.saveApprovalOutboxMessage(
                orderDataMapper.orderPaidEventToOrderApprovalEventPayload(orderPaidEvent),
                orderPaidEvent.getOrder().getOrderStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(paymentResponse.getSagaID())
        );

        log.info("Order with id: {} is paid", order.getId().getValue().toString());
    }

    @Override
    @Transactional
    public void rollback(PaymentResponse paymentResponse) {
        log.info("Cancelling order with id {}", paymentResponse.getOrderID());
        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderID());
        orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} is cancelled", order.getId().getValue().toString());
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(OrderPaymentOutboxMessage outboxMessage,
                                                                     OrderStatus orderStatus,
                                                                     SagaStatus sagaStatus) {
        outboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        outboxMessage.setOrderStatus(orderStatus);
        outboxMessage.setSagaStatus(sagaStatus);
        return outboxMessage;
    }
}
