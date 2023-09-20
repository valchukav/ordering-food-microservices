package ru.avalc.ordering.service.domain.ports.output.message.publisher.payment;

import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import ru.avalc.ordering.service.domain.ports.output.message.publisher.RequestMessagePublisher;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

public interface PaymentRequestMessagePublisher extends RequestMessagePublisher<OrderPaymentOutboxMessage> {

}
