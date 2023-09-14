package ru.avalc.ordering.restaurant.service.dto;

import lombok.Builder;
import lombok.Getter;
import ru.avalc.ordering.restaurant.service.domain.entity.Product;
import ru.avalc.ordering.system.domain.valueobject.RestaurantOrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class RestaurantApprovalRequest {

    private final String id;
    private final String sagaID;
    private final String restaurantID;
    private final String orderID;
    private final RestaurantOrderStatus restaurantOrderStatus;
    private final List<Product> products;
    private final BigDecimal price;
    private final Instant createdAt;

    @Builder
    private RestaurantApprovalRequest(String id, String sagaID, String restaurantID, String orderID, RestaurantOrderStatus restaurantOrderStatus, List<Product> products, BigDecimal price, Instant createdAt) {
        this.id = id;
        this.sagaID = sagaID;
        this.restaurantID = restaurantID;
        this.orderID = orderID;
        this.restaurantOrderStatus = restaurantOrderStatus;
        this.products = products;
        this.price = price;
        this.createdAt = createdAt;
    }
}
