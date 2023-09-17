package ru.avalc.ordering.service.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.avalc.ordering.application.dto.create.CreateOrderCommand;
import ru.avalc.ordering.domain.OrderDomainService;
import ru.avalc.ordering.domain.entity.Customer;
import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.entity.Restaurant;
import ru.avalc.ordering.domain.event.OrderCreatedEvent;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.service.domain.mapper.OrderDataMapper;
import ru.avalc.ordering.service.domain.ports.output.repository.CustomerRepository;
import ru.avalc.ordering.service.domain.ports.output.repository.OrderRepository;
import ru.avalc.ordering.service.domain.ports.output.repository.RestaurantRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Slf4j
@Component
@AllArgsConstructor
public class OrderCreateHelper {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerID());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        saveOrder(order);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        return orderCreatedEvent;
    }

    private void checkCustomer(UUID customerID) {
        Optional<Customer> customer = customerRepository.findCustomer(customerID);
        if (customer.isEmpty()) {
            log.warn("Could not find customer with id: {}", customerID);
            throw new OrderDomainException("Could not find customer with id: " + customerID);
        }
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> restaurantInformation = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantInformation.isEmpty()) {
            log.warn("Could not find restaurant with id: {}", createOrderCommand.getRestaurantID());
            throw new OrderDomainException("Could not find restaurant with id: " + createOrderCommand.getRestaurantID());
        } else return restaurantInformation.get();
    }

    private Order saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        if (savedOrder == null) {
            log.error("Could not save order");
            throw new OrderDomainException("Could not save order");
        } else {
            log.info("Order is saved with id: " + savedOrder.getId().getValue());
            return savedOrder;
        }
    }
}
