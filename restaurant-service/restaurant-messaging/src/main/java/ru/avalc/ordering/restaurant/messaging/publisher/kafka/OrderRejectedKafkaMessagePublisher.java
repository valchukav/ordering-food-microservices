package ru.avalc.ordering.restaurant.messaging.publisher.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import ru.avalc.ordering.kafka.producer.KafkaMessageHelper;
import ru.avalc.ordering.kafka.producer.service.KafkaProducer;
import ru.avalc.ordering.restaurant.messaging.mapper.RestaurantMessagingDataMapper;
import ru.avalc.ordering.restaurant.service.domain.config.RestaurantServiceConfig;
import ru.avalc.ordering.restaurant.service.domain.event.OrderRejectedEvent;
import ru.avalc.ordering.restaurant.service.domain.ports.output.OrderRejectedMessagePublisher;

/**
 * @author Alexei Valchuk, 15.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class OrderRejectedKafkaMessagePublisher implements OrderRejectedMessagePublisher {

    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfig restaurantServiceConfig;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderRejectedEvent domainEvent) {
        String orderID = domainEvent.getOrderApproval().getOrderID().getValue().toString();

        log.info("Received OrderApprovedEvent for order id: {}", orderID);

        try {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel
                    = restaurantMessagingDataMapper.orderApprovalEventToRestaurantApprovalResponseAvroModel(domainEvent);

            kafkaProducer.send(
                    restaurantServiceConfig.getRestaurantApprovalResponseTopicName(),
                    orderID,
                    restaurantApprovalResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            restaurantServiceConfig.getRestaurantApprovalResponseTopicName(),
                            restaurantApprovalResponseAvroModel,
                            orderID,
                            "OrderRejectedEvent"
                    ));

            log.info("RestaurantApprovalResponseAvroModel sent to Kafka for order id: {}", orderID);
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalResponseAvroModel to Kafka with order id: {}, error: {}", orderID, e.getMessage());
        }
    }
}
