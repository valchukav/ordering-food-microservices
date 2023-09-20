package ru.avalc.ordering.order.service.messaging.listener.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.domain.exception.OrderNotFoundException;
import ru.avalc.ordering.kafka.consumer.KafkaConsumer;
import ru.avalc.ordering.kafka.order.avro.model.OrderApprovalStatus;
import ru.avalc.ordering.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import ru.avalc.ordering.order.service.messaging.mapper.OrderMessagingDataMapper;
import ru.avalc.ordering.service.domain.ports.input.message.listener.restaurant.RestaurantApprovalResponseMessageListener;

import java.util.List;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of restaurant approval responses received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString()
        );

        messages.forEach(restaurantApprovalResponseAvroModel -> {
            try {
                if (restaurantApprovalResponseAvroModel.getOrderApprovalStatus() == OrderApprovalStatus.APPROVED) {
                    log.info("Processing approved order for order id: {}", restaurantApprovalResponseAvroModel.getOrderId());

                    restaurantApprovalResponseMessageListener.orderApproved(orderMessagingDataMapper
                            .approvalResponseAvroModelToRestaurantApprovalResponse(restaurantApprovalResponseAvroModel));
                } else if (restaurantApprovalResponseAvroModel.getOrderApprovalStatus() == OrderApprovalStatus.REJECTED) {
                    log.info("Processing rejected order for order id: {}", restaurantApprovalResponseAvroModel.getOrderId());
                    restaurantApprovalResponseMessageListener.orderRejected(orderMessagingDataMapper
                            .approvalResponseAvroModelToRestaurantApprovalResponse(restaurantApprovalResponseAvroModel));
                }
            } catch (OptimisticLockingFailureException e) {
                log.error("Caught optimistic locking exception in RestaurantApprovalResponseKafkaListener for order id: {}",
                        restaurantApprovalResponseAvroModel.getOrderId());
            } catch (OrderNotFoundException ex) {
                log.error("No order found for order id: {}",
                        restaurantApprovalResponseAvroModel.getOrderId());
            }
        });
    }
}
