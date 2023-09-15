package ru.avalc.ordering.order.service.dataaccess.restaurant.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.dataaccess.restaurant.entity.RestaurantEntity;
import ru.avalc.ordering.dataaccess.restaurant.exception.RestaurantDataAccessException;
import ru.avalc.ordering.domain.entity.Product;
import ru.avalc.ordering.domain.entity.Restaurant;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.ProductID;
import ru.avalc.ordering.system.domain.valueobject.RestaurantID;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntitiesToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity
                = restaurantEntities.stream().findFirst().orElseThrow(() -> new RestaurantDataAccessException("Restaurant could not be found"));

        List<Product> products = restaurantEntities.stream()
                .map(entity ->
                        Product.builder()
                                .productID(new ProductID(entity.getProductID()))
                                .price(new Money(entity.getProductPrice()))
                                .build()
                ).toList();

        return Restaurant.builder()
                .restaurantID(new RestaurantID(restaurantEntity.getRestaurantID()))
                .products(products)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}
