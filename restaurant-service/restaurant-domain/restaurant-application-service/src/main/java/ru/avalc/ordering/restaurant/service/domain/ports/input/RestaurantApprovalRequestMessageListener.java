package ru.avalc.ordering.restaurant.service.domain.ports.input;

import ru.avalc.ordering.restaurant.service.dto.RestaurantApprovalRequest;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

public interface RestaurantApprovalRequestMessageListener {

    void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest);
}
