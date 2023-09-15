package ru.avalc.ordering.order.service.dataaccess.order.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.valueobject.TrackingID;
import ru.avalc.ordering.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import ru.avalc.ordering.order.service.dataaccess.order.repository.OrderJpaRepository;
import ru.avalc.ordering.service.domain.ports.output.repository.OrderRepository;
import ru.avalc.ordering.system.domain.valueobject.OrderID;

import java.util.Optional;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Component
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper mapper;

    @Override
    public Order save(Order order) {
        return mapper.orderEntityToOrder(orderJpaRepository.save(mapper.orderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findByTrackingID(TrackingID trackingID) {
        return orderJpaRepository.findByTrackingID(trackingID.getValue())
                .map(mapper::orderEntityToOrder);
    }

    @Override
    public Optional<Order> findByOrderID(OrderID orderID) {
        return orderJpaRepository.findById(orderID.getValue()).map(mapper::orderEntityToOrder);
    }
}
