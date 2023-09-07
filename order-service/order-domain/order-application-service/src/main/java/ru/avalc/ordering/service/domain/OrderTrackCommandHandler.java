package ru.avalc.ordering.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.exception.OrderNotFoundException;
import ru.avalc.ordering.domain.valueobject.TrackingID;
import ru.avalc.ordering.service.domain.dto.track.TrackOrderQuery;
import ru.avalc.ordering.service.domain.dto.track.TrackOrderResponse;
import ru.avalc.ordering.service.domain.mapper.OrderDataMapper;
import ru.avalc.ordering.service.domain.ports.output.repository.OrderRepository;

import java.util.Optional;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        Optional<Order> order = orderRepository
                .findByTrackingID(new TrackingID(trackOrderQuery.getOrderTrackingID()));
        if (order.isEmpty()) {
            log.warn("Could not find order with tracking id: {}", trackOrderQuery.getOrderTrackingID());
            throw new OrderNotFoundException("Could not find order with tracking id: " + trackOrderQuery.getOrderTrackingID());
        }

        return orderDataMapper.orderToTrackOrderResponse(order.get());
    }
}
