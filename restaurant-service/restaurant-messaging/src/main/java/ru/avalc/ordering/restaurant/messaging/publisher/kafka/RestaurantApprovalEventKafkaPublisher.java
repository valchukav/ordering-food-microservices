package ru.avalc.ordering.restaurant.messaging.publisher.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import ru.avalc.ordering.kafka.producer.KafkaMessageHelper;
import ru.avalc.ordering.kafka.producer.service.KafkaProducer;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.restaurant.messaging.mapper.RestaurantMessagingDataMapper;
import ru.avalc.ordering.restaurant.service.domain.config.RestaurantServiceConfigData;
import ru.avalc.ordering.restaurant.service.domain.outbox.model.OrderEventPayload;
import ru.avalc.ordering.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import ru.avalc.ordering.restaurant.service.domain.ports.output.publisher.ApprovalResponseMessagePublisher;

import java.util.function.BiConsumer;

/**
 * @author Alexei Valchuk, 20.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class RestaurantApprovalEventKafkaPublisher implements ApprovalResponseMessagePublisher {

    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfigData restaurantServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {
        OrderEventPayload orderEventPayload =
                kafkaMessageHelper.getOrderEventPayload(orderOutboxMessage.getPayload(), OrderEventPayload.class);

        String sagaId = orderOutboxMessage.getSagaID().toString();

        log.info("Received OrderOutboxMessage for order id: {} and saga id: {}", orderEventPayload.getOrderID(), sagaId);

        try {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel =
                    restaurantMessagingDataMapper
                            .orderEventPayloadToRestaurantApprovalResponseAvroModel(sagaId, orderEventPayload);

            kafkaProducer.send(restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                    sagaId,
                    restaurantApprovalResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(restaurantServiceConfigData
                                    .getRestaurantApprovalResponseTopicName(),
                            restaurantApprovalResponseAvroModel,
                            orderOutboxMessage,
                            outboxCallback,
                            orderEventPayload.getOrderID(),
                            "RestaurantApprovalResponseAvroModel"));

            log.info("RestaurantApprovalResponseAvroModel sent to kafka for order id: {} and saga id: {}",
                    restaurantApprovalResponseAvroModel.getOrderId(), sagaId);
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalResponseAvroModel message" +
                    " to kafka with order id: {} and saga id: {}, error: {}", orderEventPayload.getOrderID(), sagaId, e.getMessage());
        }
    }
}
