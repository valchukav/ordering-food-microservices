package ru.avalc.ordering.service.domain.ports.output.repository;

import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.valueobject.TrackingID;
import ru.avalc.ordering.system.domain.valueobject.OrderID;

import java.util.Optional;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByTrackingID(TrackingID trackingID);

    Optional<Order> findByOrderID(OrderID orderID);
}
