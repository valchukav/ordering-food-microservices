package ru.avalc.ordering.restaurant.service.domain.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.restaurant.service.domain.entity.OrderDetail;
import ru.avalc.ordering.restaurant.service.domain.entity.Product;
import ru.avalc.ordering.restaurant.service.domain.entity.Restaurant;
import ru.avalc.ordering.restaurant.service.domain.event.OrderApprovalEvent;
import ru.avalc.ordering.restaurant.service.domain.outbox.model.OrderEventPayload;
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

    public OrderEventPayload orderApprovalEventToOrderEventPayload(OrderApprovalEvent orderApprovalEvent) {
        return OrderEventPayload.builder()
                .orderID(orderApprovalEvent.getOrderApproval().getOrderID().getValue().toString())
                .restaurantID(orderApprovalEvent.getRestaurantID().getValue().toString())
                .orderApprovalStatus(orderApprovalEvent.getOrderApproval().getOrderApprovalStatus().name())
                .createdAt(orderApprovalEvent.getCreatedAt())
                .failureMessages(orderApprovalEvent.getFailureMessages())
                .build();
    }
}
