package ru.avalc.ordering.order.service.messaging.publisher.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.domain.event.OrderPaidEvent;
import ru.avalc.ordering.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import ru.avalc.ordering.kafka.producer.service.KafkaProducer;
import ru.avalc.ordering.order.service.messaging.mapper.OrderMessagingDataMapper;
import ru.avalc.ordering.service.domain.config.OrderServiceConfigData;
import ru.avalc.ordering.service.domain.ports.output.message.publisher.restaurant.OrderPaidRestaurantRequestMessagePublisher;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final OrderKafkaMessageHelper orderKafkaMessageHelper;

    @Override
    public void publish(OrderPaidEvent domainEvent) {
        String orderID = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderPaidEvent for order id: {}", orderID);

        try {
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel
                    = orderMessagingDataMapper.orderPaidEventToRestaurantApprovalRequestAvroModel(domainEvent);

            kafkaProducer.send(
                    orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    orderID,
                    restaurantApprovalRequestAvroModel,
                    orderKafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getPaymentResponseTopicName(),
                            restaurantApprovalRequestAvroModel,
                            orderID,
                            "RestaurantApprovalRequestAvroModel"
                    )
            );

            log.info("RestaurantApprovalRequestAvroModel send to Kafka for order id: {}", orderID);
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalRequestAvroModel message to Kafka with order id: {}, error: {}",
                    orderID, e.getMessage()
            );
        }
    }
}
