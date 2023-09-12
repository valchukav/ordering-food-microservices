package ru.avalc.ordering.order.service.dataaccess.restaurant.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 11.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntityID implements Serializable {

    private UUID restaurantID;
    private UUID productID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantEntityID that)) return false;
        return Objects.equals(getRestaurantID(), that.getRestaurantID()) && Objects.equals(getProductID(), that.getProductID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRestaurantID(), getProductID());
    }
}
