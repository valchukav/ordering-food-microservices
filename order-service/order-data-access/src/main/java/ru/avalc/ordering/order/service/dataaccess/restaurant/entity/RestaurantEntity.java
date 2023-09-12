package ru.avalc.ordering.order.service.dataaccess.restaurant.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Table(name = "order_restaurant_m_view", schema = "restaurant")
@IdClass(RestaurantEntityID.class)
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity {

    @Id
    private UUID restaurantID;

    @Id
    private UUID productID;

    private String restaurantName;
    private Boolean restaurantActive;
    private String productName;
    private BigDecimal productPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantEntity that)) return false;
        return Objects.equals(getRestaurantID(), that.getRestaurantID()) && Objects.equals(getProductID(), that.getProductID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRestaurantID(), getProductID());
    }
}
