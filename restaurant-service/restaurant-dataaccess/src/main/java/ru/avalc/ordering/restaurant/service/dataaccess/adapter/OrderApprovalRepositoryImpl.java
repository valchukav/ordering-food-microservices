package ru.avalc.ordering.restaurant.service.dataaccess.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.avalc.ordering.restaurant.service.dataaccess.mapper.RestaurantDataAccessMapper;
import ru.avalc.ordering.restaurant.service.dataaccess.repository.OrderApprovalJpaRepository;
import ru.avalc.ordering.restaurant.service.domain.entity.OrderApproval;
import ru.avalc.ordering.restaurant.service.domain.ports.output.OrderApprovalRepository;

/**
 * @author Alexei Valchuk, 15.09.2023, email: a.valchukav@gmail.com
 */

@Component
@AllArgsConstructor
public class OrderApprovalRepositoryImpl implements OrderApprovalRepository {

    private final OrderApprovalJpaRepository orderApprovalJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        return restaurantDataAccessMapper
                .orderApprovalEntityToOrderApproval(orderApprovalJpaRepository
                        .save(restaurantDataAccessMapper.orderApprovalToOrderApprovalEntity(orderApproval)));
    }
}
