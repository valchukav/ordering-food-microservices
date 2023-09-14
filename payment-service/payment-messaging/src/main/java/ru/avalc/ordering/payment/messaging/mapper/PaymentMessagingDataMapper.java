package ru.avalc.ordering.payment.messaging.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.kafka.order.avro.model.PaymentRequestAvroModel;
import ru.avalc.ordering.kafka.order.avro.model.PaymentResponseAvroModel;
import ru.avalc.ordering.kafka.order.avro.model.PaymentStatus;
import ru.avalc.ordering.payment.service.domain.event.PaymentEvent;
import ru.avalc.ordering.payment.service.dto.PaymentRequest;
import ru.avalc.ordering.system.domain.valueobject.PaymentOrderStatus;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class PaymentMessagingDataMapper {

    public PaymentResponseAvroModel paymentEventToPaymentResponseAvroModel(PaymentEvent paymentEvent) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(paymentEvent.getPayment().getId().getValue().toString())
                .setCustomerId(paymentEvent.getPayment().getCustomerID().getValue().toString())
                .setOrderId(paymentEvent.getPayment().getOrderID().getValue().toString())
                .setPrice(paymentEvent.getPayment().getPrice().getAmount())
                .setCreatedAt(paymentEvent.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(paymentEvent.getPayment().getPaymentStatus().toString()))
                .setFailureMessages(paymentEvent.getFailureMessages())
                .build();
    }

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
}
