package ru.avalc.ordering.service.domain.ports.input.message.listener.payment;

import ru.avalc.ordering.service.domain.dto.message.PaymentResponse;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

public interface PaymentResponseMessageListener {

    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCanceled(PaymentResponse paymentResponse);
}
