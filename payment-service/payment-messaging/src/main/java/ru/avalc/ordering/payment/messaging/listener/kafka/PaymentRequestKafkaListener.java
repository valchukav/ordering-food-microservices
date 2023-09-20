package ru.avalc.ordering.payment.messaging.listener.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.kafka.consumer.KafkaConsumer;
import ru.avalc.ordering.kafka.order.avro.model.PaymentOrderStatus;
import ru.avalc.ordering.kafka.order.avro.model.PaymentRequestAvroModel;
import ru.avalc.ordering.payment.messaging.mapper.PaymentMessagingDataMapper;
import ru.avalc.ordering.payment.service.domain.exception.PaymentNotFoundException;
import ru.avalc.payment.service.domain.exception.PaymentApplicationServiceException;
import ru.avalc.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;

import java.sql.SQLException;
import java.util.List;

import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {

    private final PaymentRequestMessageListener paymentRequestMessageListener;
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${payment-service.payment-request-topic-name}")
    public void receive(@Payload List<PaymentRequestAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of payment requests received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString()
        );

        messages.forEach(paymentRequestAvroModel -> {
            try {
                if (paymentRequestAvroModel.getPaymentOrderStatus() == PaymentOrderStatus.PENDING) {
                    log.info("Proceed payment for order id: {}", paymentRequestAvroModel.getOrderId());
                    paymentRequestMessageListener.completePayment(
                            paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel)
                    );
                } else if (paymentRequestAvroModel.getPaymentOrderStatus() == PaymentOrderStatus.CANCELLED) {
                    log.info("Cancelling payment for order id: {}", paymentRequestAvroModel.getOrderId());
                    paymentRequestMessageListener.cancelPayment(
                            paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel)
                    );
                }
            } catch (DataAccessException e) {
                SQLException sqlException = (SQLException) e.getRootCause();
                if (sqlException != null && sqlException.getSQLState() != null && sqlException.getSQLState().equals(UNIQUE_VIOLATION.getState())) {
                    log.error("Caught unique constraint exception with sql state: {} " +
                            "in PaymentRequestKafkaListener for order id: {}", sqlException.getSQLState(), paymentRequestAvroModel.getOrderId());
                } else {
                    throw new PaymentApplicationServiceException("Throwing DataAccessException in" +
                            " PaymentRequestKafkaListener: " + e.getMessage(), e);
                }
            } catch (PaymentNotFoundException e) {
                log.error("No payment found for order id: {}", paymentRequestAvroModel.getOrderId());
            }
        });
    }
}
