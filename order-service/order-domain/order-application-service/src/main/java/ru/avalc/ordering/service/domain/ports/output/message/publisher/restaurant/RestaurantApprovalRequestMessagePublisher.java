package ru.avalc.ordering.service.domain.ports.output.message.publisher.restaurant;

import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import ru.avalc.ordering.service.domain.ports.output.message.publisher.RequestMessagePublisher;

/**
 * @author Alexei Valchuk, 16.09.2023, email: a.valchukav@gmail.com
 */

public interface RestaurantApprovalRequestMessagePublisher extends RequestMessagePublisher<OrderApprovalOutboxMessage> {

}
