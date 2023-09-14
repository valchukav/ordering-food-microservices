package ru.avalc.ordering.restaurant.service.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.system.domain.entity.BaseEntity;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.ProductID;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class Product extends BaseEntity<ProductID> {

    @Setter
    private String name;
    @Setter
    private Money price;
    private final int quantity;
    @Setter
    private boolean available;

    public void updateWithConfirmedNamePriceAndAvailability(String name, Money price, boolean available) {
        this.name = name;
        this.price = price;
        this.available = available;
    }

    @Builder
    private Product(ProductID productID, String name, Money price, int quantity, boolean available) {
        super(productID);
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.available = available;
    }
}
