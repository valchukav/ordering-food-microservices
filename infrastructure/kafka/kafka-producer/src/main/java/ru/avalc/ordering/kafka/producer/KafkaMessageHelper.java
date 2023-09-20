package ru.avalc.ordering.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.outbox.OutboxStatus;

import java.util.function.BiConsumer;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class KafkaMessageHelper {

    private final ObjectMapper objectMapper;

    public <T, U> ListenableFutureCallback<SendResult<String, T>> getKafkaCallback(String paymentResponseTopicName,
                                                                                   T avroModel, U outboxMessage,
                                                                                   BiConsumer<U, OutboxStatus> outboxCallback,
                                                                                   String orderID, String modelName) {
        return new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending {} with message: {} and outbox type {} to topic: {}",
                        modelName, avroModel.toString(), outboxMessage.getClass().getSimpleName(), paymentResponseTopicName, ex);
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

    public <T> T getOrderEventPayload(String payload, Class<T> clazz) {
        try {
            return objectMapper.readValue(payload, clazz);
        } catch (JsonProcessingException e) {
            String message = "Could not read " + clazz.getName();
            log.error(message, e);
            throw new OrderDomainException(message, e);
        }
    }
}
