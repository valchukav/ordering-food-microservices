package ru.avalc.ordering.payment.messaging.publisher.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.kafka.order.avro.model.PaymentResponseAvroModel;
import ru.avalc.ordering.kafka.producer.KafkaMessageHelper;
import ru.avalc.ordering.kafka.producer.service.KafkaProducer;
import ru.avalc.ordering.payment.messaging.mapper.PaymentMessagingDataMapper;
import ru.avalc.ordering.payment.service.domain.event.PaymentCompletedEvent;
import ru.avalc.payment.service.domain.config.PaymentServiceConfigData;
import ru.avalc.payment.service.domain.ports.output.repository.message.publisher.PaymentCompletedMessagePublisher;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class PaymentCompletedKafkaMessagePublisher implements PaymentCompletedMessagePublisher {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(PaymentCompletedEvent domainEvent) {
        String orderID = domainEvent.getPayment().getOrderID().getValue().toString();
        log.info("Received PaymentCompletedEvent for order id: {}", orderID);

        try {
            PaymentResponseAvroModel paymentResponseAvroModel = paymentMessagingDataMapper.paymentEventToPaymentResponseAvroModel(domainEvent);
            kafkaProducer.send(
                    paymentServiceConfigData.getPaymentResponseTopicName(),
                    orderID,
                    paymentResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(paymentServiceConfigData.getPaymentResponseTopicName(),
                            paymentResponseAvroModel,
                            orderID,
                            "PaymentResponseAvroModel")
            );

            log.info("PaymentResponseAvroModel sent to Kafka for order id: {}", orderID);
        } catch (Exception e) {
            log.error("Error while sending PaymentResponseAvroModel to Kafka with order id: {}, error: {}", orderID, e.getMessage());
        }
    }
}
