package ru.avalc.ordering.service.domain.ports.output.repository;

import ru.avalc.ordering.domain.entity.Restaurant;

import java.util.Optional;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
