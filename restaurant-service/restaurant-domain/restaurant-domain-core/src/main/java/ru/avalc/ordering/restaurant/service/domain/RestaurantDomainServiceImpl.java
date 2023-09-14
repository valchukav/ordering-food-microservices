package ru.avalc.ordering.restaurant.service.domain;

import lombok.extern.slf4j.Slf4j;
import ru.avalc.ordering.restaurant.service.domain.entity.Restaurant;
import ru.avalc.ordering.restaurant.service.domain.event.OrderApprovalEvent;
import ru.avalc.ordering.restaurant.service.domain.event.OrderApprovedEvent;
import ru.avalc.ordering.restaurant.service.domain.event.OrderRejectedEvent;
import ru.avalc.ordering.system.domain.event.publisher.DomainEventPublisher;
import ru.avalc.ordering.system.domain.valueobject.OrderApprovalStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static ru.avalc.ordering.system.domain.DomainConstants.UTC;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
public class RestaurantDomainServiceImpl implements RestaurantDomainService {

    @Override
    public OrderApprovalEvent validateOrder(Restaurant restaurant, List<String> failureMessages,
                                            DomainEventPublisher<OrderApprovalEvent> orderApprovalEventDomainEventPublisher,
                                            DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher) {
        restaurant.validateOrder(failureMessages);
        log.info("Validating order with id: {}", restaurant.getOrderDetail().getId().getValue());

        if (failureMessages.isEmpty()) {
            log.info("Order is approved for order id: {}", restaurant.getOrderDetail().getId().getValue());
            restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(
                    restaurant.getOrderApproval(),
                    restaurant.getId(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(UTC)),
                    orderApprovalEventDomainEventPublisher
            );
        } else {
            log.info("Order is rejected for order id: {}", restaurant.getOrderDetail().getId().getValue());
            return new OrderRejectedEvent(restaurant.getOrderApproval(),
                    restaurant.getId(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(UTC)),
                    orderRejectedEventDomainEventPublisher
            );
        }
    }
}
