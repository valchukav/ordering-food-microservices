package ru.avalc.ordering.customer.service.messaging.publisher.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.avalc.customer.service.domain.event.CustomerCreatedEvent;
import ru.avalc.ordering.customer.service.domain.config.CustomerServiceConfigData;
import ru.avalc.ordering.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher;
import ru.avalc.ordering.customer.service.messaging.mapper.CustomerMessagingDataMapper;
import ru.avalc.ordering.kafka.order.avro.model.CustomerAvroModel;
import ru.avalc.ordering.kafka.producer.service.KafkaProducer;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class CustomerCreatedEventKafkaPublisher implements CustomerMessagePublisher {

    private final CustomerMessagingDataMapper customerMessagingDataMapper;
    private final KafkaProducer<String, CustomerAvroModel> kafkaProducer;
    private final CustomerServiceConfigData customerServiceConfigData;

    @Override
    public void publish(CustomerCreatedEvent customerCreatedEvent) {
        log.info("Received CustomerCreatedEvent for customer id: {}", customerCreatedEvent.getCustomer().getId().getValue());

        try {
            CustomerAvroModel customerAvroModel = customerMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(customerCreatedEvent);

            kafkaProducer.send(
                    customerServiceConfigData.getCustomerTopicName(),
                    customerAvroModel.getId(),
                    customerAvroModel,
                    getCallback(customerServiceConfigData.getCustomerTopicName(), customerAvroModel));

            log.info("CustomerCreatedEvent sent to kafka for customer id: {}", customerAvroModel.getId());
        } catch (Exception e) {
            log.error("Error while sending CustomerCreatedEvent to kafka for customer id: {}," +
                    " error: {}", customerCreatedEvent.getCustomer().getId().getValue(), e.getMessage());
        }
    }

    private ListenableFutureCallback<SendResult<String, CustomerAvroModel>> getCallback(String topicName, CustomerAvroModel message) {
        return new ListenableFutureCallback<>() {

            @Override
            public void onFailure(Throwable throwable) {
                log.error("Error while sending message {} to topic {}", message.toString(), topicName, throwable);
            }

            @Override
            public void onSuccess(SendResult<String, CustomerAvroModel> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}, at time {}",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp(),
                        System.nanoTime());
            }
        };
    }
}
