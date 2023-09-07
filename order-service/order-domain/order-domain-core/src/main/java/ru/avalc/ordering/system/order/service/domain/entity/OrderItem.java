package ru.avalc.ordering.system.order.service.domain.entity;

import lombok.Builder;
import lombok.Getter;
import ru.avalc.ordering.system.domain.entity.BaseEntity;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.OrderID;
import ru.avalc.ordering.system.order.service.domain.valueobject.OrderItemID;

/**
 * @author Alexei Valchuk, 06.09.2023, email: a.valchukav@gmail.com
 */

@Builder
@Getter
public class OrderItem extends BaseEntity<OrderItemID> {

    private OrderID orderID;
    private final Product product;
    private final int quantity;
    private final Money price;
    private final Money subTotal;

    private OrderItem(OrderItemID orderItemID, OrderID orderID, Product product, int quantity, Money price, Money subTotal) {
        super(orderItemID);
        this.orderID = orderID;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = subTotal;
    }

    protected void initOrderItem(OrderID orderID, OrderItemID orderItemID) {
        this.orderID = orderID;
        super.setId(orderItemID);
    }

    protected boolean isPriceValid() {
        return price.isGreaterThanZero()
                && price.equals(product.getPrice())
                && price.multiply(quantity).equals(subTotal);
    }
}
