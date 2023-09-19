package ru.avalc.ordering.payment.messaging.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.kafka.order.avro.model.PaymentRequestAvroModel;
import ru.avalc.ordering.kafka.order.avro.model.PaymentResponseAvroModel;
import ru.avalc.ordering.kafka.order.avro.model.PaymentStatus;
import ru.avalc.ordering.payment.service.dto.PaymentRequest;
import ru.avalc.ordering.system.domain.valueobject.PaymentOrderStatus;
import ru.avalc.payment.service.domain.outbox.model.OrderEventPayload;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class PaymentMessagingDataMapper {

    public PaymentRequest paymentRequestAvroModelToPaymentRequest(PaymentRequestAvroModel paymentRequestAvroModel) {
        return PaymentRequest.builder()
                .id(paymentRequestAvroModel.getId())
                .sagaID(paymentRequestAvroModel.getSagaId())
                .customerID(paymentRequestAvroModel.getCustomerId())
                .orderID(paymentRequestAvroModel.getOrderId())
                .price(paymentRequestAvroModel.getPrice())
                .createdAt(paymentRequestAvroModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(paymentRequestAvroModel.getPaymentOrderStatus().toString()))
                .build();
    }

    public PaymentResponseAvroModel orderEventPayloadToPaymentResponseAvroModel(String sagaId, OrderEventPayload orderEventPayload) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setPaymentId(orderEventPayload.getPaymentID())
                .setCustomerId(orderEventPayload.getCustomerID())
                .setOrderId(orderEventPayload.getOrderID())
                .setPrice(orderEventPayload.getPrice())
                .setCreatedAt(orderEventPayload.getCreatedAt().toInstant())//??
                .setPaymentStatus(PaymentStatus.valueOf(orderEventPayload.getPaymentStatus()))
                .setFailureMessages(orderEventPayload.getFailureMessages())
                .build();
    }
}
