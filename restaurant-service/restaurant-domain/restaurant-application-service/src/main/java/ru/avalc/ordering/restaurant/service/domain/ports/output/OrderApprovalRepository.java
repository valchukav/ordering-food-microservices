package ru.avalc.ordering.restaurant.service.domain.ports.output;

import ru.avalc.ordering.restaurant.service.domain.entity.OrderApproval;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

public interface OrderApprovalRepository {

    OrderApproval save(OrderApproval orderApproval);
}
