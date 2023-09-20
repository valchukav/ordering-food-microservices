package ru.avalc.ordering.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.application.dto.create.CreateOrderCommand;
import ru.avalc.ordering.application.dto.create.CreateOrderResponse;
import ru.avalc.ordering.domain.event.OrderCreatedEvent;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.service.domain.mapper.OrderDataMapper;
import ru.avalc.ordering.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final OrderSagaHelper orderSagaHelper;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        CreateOrderResponse createOrderResponse
                = orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(), "Order created successfully");

        paymentOutboxHelper.savePaymentOutboxMessage(
                orderDataMapper.orderCreatedEventToOrderPaymentEventPayload(orderCreatedEvent),
                orderCreatedEvent.getOrder().getOrderStatus(),
                orderSagaHelper.orderStatusToSagaStatus(orderCreatedEvent.getOrder().getOrderStatus()),
                OutboxStatus.STARTED,
                UUID.randomUUID()
        );

        log.info("Returning CreateOrderResponse with order id: {}", orderCreatedEvent.getOrder().getId().getValue());

        return createOrderResponse;
    }
}
