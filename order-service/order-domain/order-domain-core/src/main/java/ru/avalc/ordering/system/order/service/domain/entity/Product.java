package ru.avalc.ordering.system.order.service.domain.entity;

import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.system.domain.entity.BaseEntity;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.ProductID;

/**
 * @author Alexei Valchuk, 06.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Setter
public class Product extends BaseEntity<ProductID> {

    private String name;
    private Money price;

    public Product(ProductID productID, String name, Money price) {
        super(productID);
        this.name = name;
        this.price = price;
    }
}
