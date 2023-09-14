package ru.avalc.ordering.restaurant.service.domain.ports.output;

import ru.avalc.ordering.restaurant.service.domain.entity.Restaurant;

import java.util.Optional;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
