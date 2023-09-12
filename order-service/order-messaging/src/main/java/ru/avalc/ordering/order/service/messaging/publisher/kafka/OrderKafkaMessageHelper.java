package ru.avalc.ordering.order.service.messaging.publisher.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
public class OrderKafkaMessageHelper {

    public <T> ListenableFutureCallback<SendResult<String, T>> getKafkaCallback(String paymentResponseTopicName, T requestAvroModel, String orderID, String modelName) {
        return new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending" + modelName + ": {}, to topic: {}",
                        requestAvroModel.toString(), paymentResponseTopicName, ex);
            }

            @Override
            public void onSuccess(SendResult<String, T> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Received successful response from Kafka for order id: {}, Topic: {}, Partition: {}, Offset: {}, Timestamp: {}",
                        orderID,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp()
                );
            }
        };
    }
}
