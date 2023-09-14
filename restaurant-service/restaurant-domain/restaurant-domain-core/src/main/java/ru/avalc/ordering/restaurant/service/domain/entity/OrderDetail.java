package ru.avalc.ordering.restaurant.service.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.system.domain.entity.BaseEntity;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.OrderID;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;

import java.util.List;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class OrderDetail extends BaseEntity<OrderID> {

    @Setter
    private OrderStatus orderStatus;
    @Setter
    private Money totalAmount;
    private final List<Product> products;

    @Builder
    private OrderDetail(OrderID orderID, OrderStatus orderStatus, Money totalAmount, List<Product> products) {
        super(orderID);
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.products = products;
    }
}
