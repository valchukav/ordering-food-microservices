package ru.avalc.ordering.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.avalc.ordering.service.domain.dto.create.CreateOrderCommand;
import ru.avalc.ordering.service.domain.dto.create.CreateOrderResponse;
import ru.avalc.ordering.service.domain.dto.track.TrackOrderQuery;
import ru.avalc.ordering.service.domain.dto.track.TrackOrderResponse;
import ru.avalc.ordering.service.domain.ports.input.service.OrderApplicationService;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Validated
@Service
@AllArgsConstructor
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCreateCommandHandler;

    private final OrderTrackCommandHandler orderTrackCommandHandler;

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }
}
