package ru.avalc.ordering.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.exception.OrderNotFoundException;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.service.domain.ports.output.repository.OrderRepository;
import ru.avalc.ordering.system.domain.valueobject.OrderID;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 15.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class OrderSagaHelper {

    private final OrderRepository orderRepository;

    Order findOrder(String orderID) {
        Optional<Order> optionalOrder = orderRepository.findByOrderID(new OrderID(UUID.fromString(orderID)));
        if (optionalOrder.isEmpty()) {
            String message = "Order with id " + orderID + " is not found";
            log.error(message);
            throw new OrderNotFoundException(message);
        }
        return optionalOrder.get();
    }

    void saveOrder(Order order) {
        orderRepository.save(order);
    }

    SagaStatus orderStatusToSagaStatus(OrderStatus orderStatus) {
        switch (orderStatus) {
            case PAID -> {
                return SagaStatus.PROCESSING;
            }
            case APPROVED -> {
                return SagaStatus.SUCCEEDED;
            }
            case CANCELLING -> {
                return SagaStatus.COMPENSATING;
            }
            case CANCELLED -> {
                return SagaStatus.COMPENSATED;
            }
            default -> {
                return SagaStatus.STARTED;
            }
        }
    }
}
