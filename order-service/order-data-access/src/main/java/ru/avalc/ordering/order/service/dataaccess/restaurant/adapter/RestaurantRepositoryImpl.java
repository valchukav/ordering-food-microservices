package ru.avalc.ordering.order.service.dataaccess.restaurant.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.domain.entity.Restaurant;
import ru.avalc.ordering.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import ru.avalc.ordering.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import ru.avalc.ordering.order.service.dataaccess.restaurant.repository.RestaurantJpaRepository;
import ru.avalc.ordering.service.domain.ports.output.repository.RestaurantRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Component
@AllArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> productsIDs = restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntityList
                = restaurantJpaRepository.findByRestaurantIDAndProductIDIn(restaurant.getId().getValue(), productsIDs);
        return restaurantEntityList.map(restaurantDataAccessMapper::restaurantEntitiesToRestaurant);
    }
}
