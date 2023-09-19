package ru.avalc.ordering.payment.messaging.publisher.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.kafka.order.avro.model.PaymentResponseAvroModel;
import ru.avalc.ordering.kafka.producer.KafkaMessageHelper;
import ru.avalc.ordering.kafka.producer.service.KafkaProducer;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.payment.messaging.mapper.PaymentMessagingDataMapper;
import ru.avalc.payment.service.domain.config.PaymentServiceConfigData;
import ru.avalc.payment.service.domain.outbox.model.OrderEventPayload;
import ru.avalc.payment.service.domain.outbox.model.OrderOutboxMessage;
import ru.avalc.payment.service.domain.ports.output.repository.message.publisher.PaymentResponseMessagePublisher;

import java.util.function.BiConsumer;

/**
 * @author Alexei Valchuk, 19.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class PaymentEventKafkaPublisher implements PaymentResponseMessagePublisher {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {
        OrderEventPayload orderEventPayload = kafkaMessageHelper.getOrderEventPayload(orderOutboxMessage.getPayload(), OrderEventPayload.class);

        String sagaID = orderOutboxMessage.getSagaID().toString();

        log.info("Received OrderOutboxMessage for order id: {} and saga id: {}", orderEventPayload.getOrderID(), sagaID);

        try {
            PaymentResponseAvroModel paymentResponseAvroModel
                    = paymentMessagingDataMapper.orderEventPayloadToPaymentResponseAvroModel(sagaID, orderEventPayload);

            kafkaProducer.send(
                    paymentServiceConfigData.getPaymentResponseTopicName(),
                    sagaID,
                    paymentResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            paymentServiceConfigData.getPaymentResponseTopicName(),
                            paymentResponseAvroModel,
                            orderOutboxMessage,
                            outboxCallback,
                            orderEventPayload.getOrderID(),
                            "PaymentResponseAvroModel"
                    )
            );

            log.info("PaymentResponseAvroModel sent to kafka for order id: {} and saga id: {}", paymentResponseAvroModel.getOrderId(), sagaID);
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel message" +
                    " to kafka with order id: {} and saga id: {}, error: {}", orderEventPayload.getOrderID(), sagaID, e.getMessage());
        }
    }
}
