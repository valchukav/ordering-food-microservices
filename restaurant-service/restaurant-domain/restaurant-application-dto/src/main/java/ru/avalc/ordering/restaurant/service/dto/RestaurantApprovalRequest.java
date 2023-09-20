package ru.avalc.ordering.restaurant.service.dto;

import lombok.AllArgsConstructor;
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

@Builder
@Getter
@AllArgsConstructor
public class RestaurantApprovalRequest {

    private final String id;
    private final String sagaID;
    private final String restaurantID;
    private final String orderID;
    private final RestaurantOrderStatus restaurantOrderStatus;
    private final List<Product> products;
    private final BigDecimal price;
    private final Instant createdAt;
}
