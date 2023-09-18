package ru.avalc.ordering.order.service.messaging.publisher.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.kafka.order.avro.model.PaymentRequestAvroModel;
import ru.avalc.ordering.kafka.producer.KafkaMessageHelper;
import ru.avalc.ordering.kafka.producer.service.KafkaProducer;
import ru.avalc.ordering.order.service.messaging.mapper.OrderMessagingDataMapper;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.service.domain.config.OrderServiceConfigData;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import ru.avalc.ordering.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;

import java.util.function.BiConsumer;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class OrderPaymentEventKafkaPublisher implements PaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(OrderPaymentOutboxMessage outboxMessage, BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback) {
        OrderPaymentEventPayload orderPaymentEventPayload = getOrderPaymentEventPayload(outboxMessage.getPayload());

        String sagaID = outboxMessage.getSagaID().toString();
        log.info("Received OrderPaymentOutboxMessage for order id: {} and saga id: {}", orderPaymentEventPayload.getOrderID(), sagaID);

        PaymentRequestAvroModel paymentRequestAvroModel
                = orderMessagingDataMapper.orderPaymentEventToPaymentRequestAvroModel(sagaID, orderPaymentEventPayload);

        try {
            kafkaProducer.send(
                    orderServiceConfigData.getPaymentRequestTopicName(),
                    sagaID,
                    paymentRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getPaymentRequestTopicName(),
                            paymentRequestAvroModel,
                            outboxMessage,
                            outboxCallback,
                            orderPaymentEventPayload.getOrderID(),
                            "PaymentRequestAvroModel"
                    )
            );

            log.info("OrderPaymentEventPayload is sent to Kafka for order id: {} and saga id: {}", orderPaymentEventPayload.getOrderID(), sagaID);
        } catch (Exception e) {
            log.error("Error while sending OrderPaymentEventPayload to Kafka with order id: {} and saga id: {}, error: {}",
                    orderPaymentEventPayload.getOrderID(), sagaID, e.getMessage());
        }
    }

    private OrderPaymentEventPayload getOrderPaymentEventPayload(String payload) {
        try {
            return objectMapper.readValue(payload, OrderPaymentEventPayload.class);
        } catch (JsonProcessingException e) {
            String message = "Could not read OrderPaymentEventPayload";
            log.error(message, e);
            throw new OrderDomainException(message, e);
        }
    }
}
