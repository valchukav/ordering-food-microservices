package ru.avalc.ordering.service.domain.ports.input.message.listener.restaurant;

import ru.avalc.ordering.service.domain.dto.message.RestaurantApprovalResponse;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

public interface RestaurantApprovalResponseMessageListener {

    void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
