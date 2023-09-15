package ru.avalc.ordering.restaurant.service.dataaccess.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.dataaccess.restaurant.entity.RestaurantEntity;
import ru.avalc.ordering.dataaccess.restaurant.exception.RestaurantDataAccessException;
import ru.avalc.ordering.restaurant.service.dataaccess.entity.OrderApprovalEntity;
import ru.avalc.ordering.restaurant.service.domain.entity.OrderApproval;
import ru.avalc.ordering.restaurant.service.domain.entity.OrderDetail;
import ru.avalc.ordering.restaurant.service.domain.entity.Product;
import ru.avalc.ordering.restaurant.service.domain.entity.Restaurant;
import ru.avalc.ordering.restaurant.service.domain.valueobject.OrderApprovalID;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.OrderID;
import ru.avalc.ordering.system.domain.valueobject.ProductID;
import ru.avalc.ordering.system.domain.valueobject.RestaurantID;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 15.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getOrderDetail().getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity =
                restaurantEntities.stream().findFirst().orElseThrow(() ->
                        new RestaurantDataAccessException("No restaurants found"));

        List<Product> restaurantProducts = restaurantEntities.stream().map(entity ->
                        Product.builder()
                                .productID(new ProductID(entity.getProductID()))
                                .name(entity.getProductName())
                                .price(new Money(entity.getProductPrice()))
                                .available(entity.getProductAvailable())
                                .build())
                .collect(Collectors.toList());

        return Restaurant.builder()
                .restaurantID(new RestaurantID(restaurantEntity.getRestaurantID()))
                .orderDetail(OrderDetail.builder()
                        .products(restaurantProducts)
                        .build())
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }

    public OrderApprovalEntity orderApprovalToOrderApprovalEntity(OrderApproval orderApproval) {
        return OrderApprovalEntity.builder()
                .id(orderApproval.getId().getValue())
                .restaurantID(orderApproval.getRestaurantID().getValue())
                .orderID(orderApproval.getOrderID().getValue())
                .status(orderApproval.getOrderApprovalStatus())
                .build();
    }

    public OrderApproval orderApprovalEntityToOrderApproval(OrderApprovalEntity orderApprovalEntity) {
        return OrderApproval.builder()
                .orderApprovalID(new OrderApprovalID(orderApprovalEntity.getId()))
                .restaurantID(new RestaurantID(orderApprovalEntity.getRestaurantID()))
                .orderID(new OrderID(orderApprovalEntity.getOrderID()))
                .orderApprovalStatus(orderApprovalEntity.getStatus())
                .build();
    }
}
