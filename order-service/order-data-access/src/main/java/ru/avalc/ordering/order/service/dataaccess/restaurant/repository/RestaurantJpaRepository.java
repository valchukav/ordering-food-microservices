package ru.avalc.ordering.order.service.dataaccess.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.avalc.ordering.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import ru.avalc.ordering.order.service.dataaccess.restaurant.entity.RestaurantEntityID;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityID> {

    Optional<List<RestaurantEntity>> findByRestaurantIDAndProductIDIn(UUID restaurantID, List<UUID> productIDs);
}
