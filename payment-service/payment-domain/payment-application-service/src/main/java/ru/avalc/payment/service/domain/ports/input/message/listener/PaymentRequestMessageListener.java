package ru.avalc.payment.service.domain.ports.input.message.listener;

import ru.avalc.ordering.payment.service.dto.PaymentRequest;

/**
 * @author Alexei Valchuk, 13.09.2023, email: a.valchukav@gmail.com
 */

public interface PaymentRequestMessageListener {

    void completePayment(PaymentRequest paymentRequest);

    void cancelPayment(PaymentRequest paymentRequest);
}
