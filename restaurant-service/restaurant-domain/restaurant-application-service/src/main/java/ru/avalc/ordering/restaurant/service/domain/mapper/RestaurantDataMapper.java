package ru.avalc.ordering.restaurant.service.domain.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.restaurant.service.domain.entity.OrderDetail;
import ru.avalc.ordering.restaurant.service.domain.entity.Product;
import ru.avalc.ordering.restaurant.service.domain.entity.Restaurant;
import ru.avalc.ordering.restaurant.service.dto.RestaurantApprovalRequest;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.OrderID;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;
import ru.avalc.ordering.system.domain.valueobject.RestaurantID;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class RestaurantDataMapper {


    public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        return Restaurant.builder()
                .restaurantID(new RestaurantID(UUID.fromString(restaurantApprovalRequest.getRestaurantID())))
                .orderDetail(OrderDetail.builder()
                        .orderID(new OrderID(UUID.fromString(restaurantApprovalRequest.getOrderID())))
                        .products(restaurantApprovalRequest.getProducts().stream().map(
                                        product -> Product.builder()
                                                .productID(product.getId())
                                                .quantity(product.getQuantity())
                                                .build())
                                .collect(Collectors.toList()))
                        .totalAmount(new Money(restaurantApprovalRequest.getPrice()))
                        .orderStatus(OrderStatus.valueOf(restaurantApprovalRequest.getRestaurantOrderStatus().name()))
                        .build())
                .build();
    }
}
