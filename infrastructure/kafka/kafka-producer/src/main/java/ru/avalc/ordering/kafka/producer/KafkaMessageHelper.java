package ru.avalc.ordering.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.avalc.ordering.outbox.OutboxStatus;

import java.util.function.BiConsumer;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
public class KafkaMessageHelper {

    public <T, U> ListenableFutureCallback<SendResult<String, T>> getKafkaCallback(String paymentResponseTopicName,
                                                                                   T avroModel, U outboxMessage,
                                                                                   BiConsumer<U, OutboxStatus> outboxCallback,
                                                                                   String orderID, String modelName) {
        return new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending {} with message: {} and outbox type {} to topic: {}",
                        modelName, avroModel.toString(), outboxMessage.getClass().getName(), paymentResponseTopicName, ex);
                outboxCallback.accept(outboxMessage, OutboxStatus.FAILED);
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

                outboxCallback.accept(outboxMessage, OutboxStatus.COMPLETED);
            }
        };
    }
}
