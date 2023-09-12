package ru.avalc.ordering.order.service.messaging.listener.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.kafka.consumer.KafkaConsumer;
import ru.avalc.ordering.kafka.order.avro.model.PaymentResponseAvroModel;
import ru.avalc.ordering.kafka.order.avro.model.PaymentStatus;
import ru.avalc.ordering.order.service.messaging.mapper.OrderMessagingDataMapper;
import ru.avalc.ordering.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;

import java.util.List;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response-topic-name}")
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of payment responses received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString()
        );

        messages.forEach(paymentResponseAvroModel -> {
            if (paymentResponseAvroModel.getPaymentStatus() == PaymentStatus.COMPLETED) {
                log.info("Processing successfully payment for order id: {}", paymentResponseAvroModel.getOrderId());

                paymentResponseMessageListener.paymentCompleted(orderMessagingDataMapper
                        .paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
            } else if (paymentResponseAvroModel.getPaymentStatus() == PaymentStatus.CANCELLED ||
                    paymentResponseAvroModel.getPaymentStatus() == PaymentStatus.FAILED) {
                log.info("Processing unsuccessfully payment for order id: {}", paymentResponseAvroModel.getOrderId());
                paymentResponseMessageListener.paymentCanceled(orderMessagingDataMapper
                        .paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
            }
        });
    }
}
