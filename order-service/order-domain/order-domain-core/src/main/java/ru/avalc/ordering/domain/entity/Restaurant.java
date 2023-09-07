package ru.avalc.ordering.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.avalc.ordering.system.domain.entity.AggregateRoot;
import ru.avalc.ordering.system.domain.valueobject.RestaurantID;

import java.util.List;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class Restaurant extends AggregateRoot<RestaurantID> {

    private final List<Product> products;
    @Setter
    private boolean active;

    @Builder
    private Restaurant(RestaurantID restaurantID, List<Product> products, boolean active) {
        super(restaurantID);
        this.products = products;
        this.active = active;
    }
}
