package ru.avalc.ordering.service.domain.ports.input.service;

import ru.avalc.ordering.service.domain.dto.create.CreateOrderCommand;
import ru.avalc.ordering.service.domain.dto.create.CreateOrderResponse;
import ru.avalc.ordering.service.domain.dto.track.TrackOrderQuery;
import ru.avalc.ordering.service.domain.dto.track.TrackOrderResponse;

import javax.validation.Valid;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
