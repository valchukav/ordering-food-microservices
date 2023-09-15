package ru.avalc.ordering.dataaccess.restaurant.entity;

import lombok.*;

import javax.persistence.*;
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
    @Column(name = "restaurant_id")
    private UUID restaurantID;

    @Id
    @Column(name = "product_id")
    private UUID productID;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "restaurant_active")
    private Boolean restaurantActive;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "product_available")
    private Boolean productAvailable;

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
