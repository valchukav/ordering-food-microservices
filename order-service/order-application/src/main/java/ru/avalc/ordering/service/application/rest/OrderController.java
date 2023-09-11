package ru.avalc.ordering.service.application.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.avalc.ordering.application.dto.create.CreateOrderCommand;
import ru.avalc.ordering.application.dto.create.CreateOrderResponse;
import ru.avalc.ordering.application.dto.track.TrackOrderQuery;
import ru.avalc.ordering.application.dto.track.TrackOrderResponse;
import ru.avalc.ordering.service.domain.ports.input.service.OrderApplicationService;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 11.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
@AllArgsConstructor
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {
        log.info("Creating order for Customer: {} at Restaurant: {}",
                createOrderCommand.getCustomerID(), createOrderCommand.getRestaurantID());

        CreateOrderResponse orderResponse = orderApplicationService.createOrder(createOrderCommand);
        log.info("Order created with tracking id: {}", orderResponse.getOrderTrackingID());

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingID(@PathVariable("id") UUID trackingID) {
        TrackOrderResponse trackOrderResponse = orderApplicationService.trackOrder(TrackOrderQuery.builder()
                .orderTrackingID(trackingID)
                .build());
        log.info("Returning order status with tracking id: {}", trackOrderResponse.getOrderTrackingID());

        return ResponseEntity.ok(trackOrderResponse);
    }
}
