package ru.avalc.ordering.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.avalc.ordering.application.dto.message.PaymentResponse;
import ru.avalc.ordering.domain.event.OrderPaidEvent;
import ru.avalc.ordering.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;

import static ru.avalc.ordering.system.domain.DomainConstants.FAILURE_MESSAGE_DELIMITER;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Validated
@Service
@AllArgsConstructor
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final OrderPaymentSaga orderPaymentSaga;

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        OrderPaidEvent orderPaidEvent = orderPaymentSaga.process(paymentResponse);
        log.info("Publishing OrderPaidEvent for order id: {}", paymentResponse.getOrderID());
        orderPaidEvent.fire();
    }

    @Override
    public void paymentCanceled(PaymentResponse paymentResponse) {
        orderPaymentSaga.rollback(paymentResponse);
        log.info("Order with id: {}, is roll backed with failure messages: {}",
                paymentResponse.getOrderID(),
                String.join(FAILURE_MESSAGE_DELIMITER, paymentResponse.getFailureMessages()));
    }
}
