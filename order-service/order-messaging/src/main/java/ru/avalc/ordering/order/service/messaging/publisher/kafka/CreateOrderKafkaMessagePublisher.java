package ru.avalc.ordering.order.service.messaging.publisher.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.domain.event.OrderCreatedEvent;
import ru.avalc.ordering.kafka.order.avro.model.PaymentRequestAvroModel;
import ru.avalc.ordering.kafka.producer.service.KafkaProducer;
import ru.avalc.ordering.order.service.messaging.mapper.OrderMessagingDataMapper;
import ru.avalc.ordering.service.domain.config.OrderServiceConfigData;
import ru.avalc.ordering.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderKafkaMessageHelper orderKafkaMessageHelper;

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        String orderID = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderCreatedEvent for order id: {}", orderID);

        try {
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper.orderCreateEventToPaymentRequestAvroModel(domainEvent);

            kafkaProducer.send(
                    orderServiceConfigData.getPaymentRequestTopicName(),
                    orderID,
                    paymentRequestAvroModel,
                    orderKafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getPaymentResponseTopicName(),
                            paymentRequestAvroModel,
                            orderID,
                            "PaymentRequestAvroModel"
                    )
            );

            log.info("PaymentRequestAvroModel send to Kafka for order id: {}", orderID);
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel message to Kafka with order id: {}, error: {}",
                    orderID, e.getMessage()
            );
        }
    }
}
