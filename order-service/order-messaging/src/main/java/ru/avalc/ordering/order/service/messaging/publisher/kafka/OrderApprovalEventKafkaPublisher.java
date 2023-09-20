package ru.avalc.ordering.order.service.messaging.publisher.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import ru.avalc.ordering.kafka.producer.KafkaMessageHelper;
import ru.avalc.ordering.kafka.producer.service.KafkaProducer;
import ru.avalc.ordering.order.service.messaging.mapper.OrderMessagingDataMapper;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.service.domain.config.OrderServiceConfigData;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import ru.avalc.ordering.service.domain.ports.output.message.publisher.restaurant.RestaurantApprovalRequestMessagePublisher;

import java.util.function.BiConsumer;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class OrderApprovalEventKafkaPublisher implements RestaurantApprovalRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderApprovalOutboxMessage outboxMessage, BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback) {
        OrderApprovalEventPayload orderApprovalEventPayload
                = kafkaMessageHelper.getOrderEventPayload(outboxMessage.getPayload(), OrderApprovalEventPayload.class);

        String sagaID = outboxMessage.getSagaID().toString();
        log.info("Received OrderApprovalOutboxMessage for order id: {} and saga id: {}", orderApprovalEventPayload.getOrderID(), sagaID);

        RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel
                = orderMessagingDataMapper.orderApprovalEventToRestaurantApprovalRequestAvroModel(sagaID, orderApprovalEventPayload);

        try {
            kafkaProducer.send(
                    orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    sagaID,
                    restaurantApprovalRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                            restaurantApprovalRequestAvroModel,
                            outboxMessage,
                            outboxCallback,
                            orderApprovalEventPayload.getOrderID(),
                            "RestaurantApprovalRequestAvroModel"
                    )
            );

            log.info("OrderApprovalEventPayload is sent to Kafka for order id: {} and saga id: {}", orderApprovalEventPayload.getOrderID(), sagaID);
        } catch (Exception e) {
            log.error("Error while sending OrderApprovalEventPayload to Kafka with order id: {} and saga id: {}, error: {}",
                    orderApprovalEventPayload.getOrderID(), sagaID, e.getMessage());
        }
    }

}
