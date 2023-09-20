package ru.avalc.ordering.order.service.messaging.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.application.dto.message.PaymentResponse;
import ru.avalc.ordering.application.dto.message.RestaurantApprovalResponse;
import ru.avalc.ordering.kafka.order.avro.model.*;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import ru.avalc.ordering.system.domain.valueobject.OrderApprovalStatus;
import ru.avalc.ordering.system.domain.valueobject.PaymentStatus;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class OrderMessagingDataMapper {

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .sagaID(paymentResponseAvroModel.getSagaId())
                .paymentID(paymentResponseAvroModel.getPaymentId())
                .customerID(paymentResponseAvroModel.getCustomerId())
                .orderID(paymentResponseAvroModel.getOrderId())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseAvroModel.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse approvalResponseAvroModelToRestaurantApprovalResponse(RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {
        return RestaurantApprovalResponse.builder()
                .id(restaurantApprovalResponseAvroModel.getId())
                .sagaID(restaurantApprovalResponseAvroModel.getSagaId())
                .restaurantID(restaurantApprovalResponseAvroModel.getRestaurantId())
                .orderID(restaurantApprovalResponseAvroModel.getOrderId())
                .createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(restaurantApprovalResponseAvroModel.getOrderApprovalStatus().name()))
                .failureMessages(restaurantApprovalResponseAvroModel.getFailureMessages())
                .build();
    }

    public PaymentRequestAvroModel orderPaymentEventToPaymentRequestAvroModel(String sagaId, OrderPaymentEventPayload
            orderPaymentEventPayload) {
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setCustomerId(orderPaymentEventPayload.getCustomerID())
                .setOrderId(orderPaymentEventPayload.getOrderID())
                .setPrice(orderPaymentEventPayload.getPrice())
                .setCreatedAt(orderPaymentEventPayload.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.valueOf(orderPaymentEventPayload.getPaymentOrderStatus()))
                .build();
    }

    public RestaurantApprovalRequestAvroModel orderApprovalEventToRestaurantApprovalRequestAvroModel(String sagaId,
                                                                                                     OrderApprovalEventPayload orderApprovalEventPayload) {
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setOrderId(orderApprovalEventPayload.getOrderID())
                .setRestaurantId(orderApprovalEventPayload.getRestaurantID())
                .setRestaurantOrderStatus(RestaurantOrderStatus
                        .valueOf(orderApprovalEventPayload.getRestaurantOrderStatus()))
                .setProducts(orderApprovalEventPayload.getProducts().stream().map(orderApprovalEventProduct ->
                        ru.avalc.ordering.kafka.order.avro.model.Product.newBuilder()
                                .setId(orderApprovalEventProduct.getId())
                                .setQuantity(orderApprovalEventProduct.getQuantity())
                                .build()).collect(Collectors.toList()))
                .setPrice(orderApprovalEventPayload.getPrice())
                .setCreatedAt(orderApprovalEventPayload.getCreatedAt().toInstant())
                .build();
    }
}
