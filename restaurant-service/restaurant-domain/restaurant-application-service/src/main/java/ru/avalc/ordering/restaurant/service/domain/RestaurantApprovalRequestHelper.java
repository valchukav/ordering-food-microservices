package ru.avalc.ordering.restaurant.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.restaurant.service.domain.entity.Restaurant;
import ru.avalc.ordering.restaurant.service.domain.event.OrderApprovalEvent;
import ru.avalc.ordering.restaurant.service.domain.exception.RestaurantNotFoundException;
import ru.avalc.ordering.restaurant.service.domain.mapper.RestaurantDataMapper;
import ru.avalc.ordering.restaurant.service.domain.ports.output.OrderApprovalRepository;
import ru.avalc.ordering.restaurant.service.domain.ports.output.OrderApprovedMessagePublisher;
import ru.avalc.ordering.restaurant.service.domain.ports.output.OrderRejectedMessagePublisher;
import ru.avalc.ordering.restaurant.service.domain.ports.output.RestaurantRepository;
import ru.avalc.ordering.restaurant.service.dto.RestaurantApprovalRequest;
import ru.avalc.ordering.system.domain.valueobject.OrderID;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class RestaurantApprovalRequestHelper {

    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantDataMapper restaurantDataMapper;
    private final RestaurantRepository restaurantRepository;
    private final OrderApprovalRepository orderApprovalRepository;
    private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;
    private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;

    @Transactional
    public OrderApprovalEvent persistOrderApproval(RestaurantApprovalRequest restaurantApprovalRequest) {
        log.info("Processing restaurant approval for order id: {}", restaurantApprovalRequest.getOrderID());
        ArrayList<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(restaurantApprovalRequest);
        OrderApprovalEvent orderApprovalEvent = restaurantDomainService.validateOrder(restaurant,
                failureMessages,
                orderApprovedMessagePublisher,
                orderRejectedMessagePublisher
        );

        orderApprovalRepository.save(restaurant.getOrderApproval());

        return orderApprovalEvent;
    }

    private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        Restaurant restaurant = restaurantDataMapper.restaurantApprovalRequestToRestaurant(restaurantApprovalRequest);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if (optionalRestaurant.isEmpty()) {
            String message = "Restaurant with id: " + restaurant.getId().getValue() + " not found";
            throw new RestaurantNotFoundException(message);
        }

        Restaurant restaurantEntity = optionalRestaurant.get();
        restaurantEntity.setActive(restaurant.isActive());
        restaurant.getOrderDetail().getProducts().forEach(product -> restaurantEntity.getOrderDetail().getProducts().forEach(p -> {
            if (p.getId().equals(product.getId())) {
                product.updateWithConfirmedNamePriceAndAvailability(
                        p.getName(),
                        p.getPrice(),
                        p.isAvailable()
                );
            }
        }));
        restaurant.getOrderDetail().setId(new OrderID(UUID.fromString(restaurantApprovalRequest.getRestaurantID())));
        return restaurant;
    }
}
