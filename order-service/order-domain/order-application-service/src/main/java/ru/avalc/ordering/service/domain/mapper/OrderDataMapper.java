package ru.avalc.ordering.service.domain.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.ordering.application.dto.create.CreateOrderCommand;
import ru.avalc.ordering.application.dto.create.CreateOrderResponse;
import ru.avalc.ordering.application.dto.create.OrderAddress;
import ru.avalc.ordering.application.dto.message.CustomerModel;
import ru.avalc.ordering.application.dto.track.TrackOrderResponse;
import ru.avalc.ordering.domain.entity.*;
import ru.avalc.ordering.domain.event.OrderCancelledEvent;
import ru.avalc.ordering.domain.event.OrderCreatedEvent;
import ru.avalc.ordering.domain.event.OrderPaidEvent;
import ru.avalc.ordering.domain.valueobject.StreetAddress;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import ru.avalc.ordering.service.domain.outbox.model.approval.OrderApprovalEventProduct;
import ru.avalc.ordering.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import ru.avalc.ordering.system.domain.valueobject.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .restaurantID(new RestaurantID(createOrderCommand.getRestaurantID()))
                .products(createOrderCommand.getItems().stream().map(orderItem ->
                                Product.builder()
                                        .productID(new ProductID(orderItem.getProductID()))
                                        .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerID(new CustomerID(createOrderCommand.getCustomerID()))
                .restaurantID(new RestaurantID(createOrderCommand.getRestaurantID()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .orderItems(orderItemsDtoToOrderItemsEntities(createOrderCommand.getItems()))
                .build();
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
        return CreateOrderResponse.builder()
                .orderTrackingID(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .message(message)
                .build();
    }

    public TrackOrderResponse orderToTrackOrderResponse(Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingID(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    public OrderPaymentEventPayload orderCreatedEventToOrderPaymentEventPayload(OrderCreatedEvent orderCreatedEvent) {
        return OrderPaymentEventPayload.builder()
                .customerID(orderCreatedEvent.getOrder().getCustomerID().getValue().toString())
                .orderID(orderCreatedEvent.getOrder().getId().getValue().toString())
                .price(orderCreatedEvent.getOrder().getPrice().getAmount())
                .createdAt(orderCreatedEvent.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.PENDING.name())
                .build();
    }

    public OrderPaymentEventPayload orderCancelledEventToOrderPaymentEventPayload(OrderCancelledEvent orderCancelledEvent) {
        return OrderPaymentEventPayload.builder()
                .customerID(orderCancelledEvent.getOrder().getCustomerID().getValue().toString())
                .orderID(orderCancelledEvent.getOrder().getId().getValue().toString())
                .price(orderCancelledEvent.getOrder().getPrice().getAmount())
                .createdAt(orderCancelledEvent.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.CANCELLED.name())
                .build();
    }

    public OrderApprovalEventPayload orderPaidEventToOrderApprovalEventPayload(OrderPaidEvent orderPaidEvent) {
        return OrderApprovalEventPayload.builder()
                .orderID(orderPaidEvent.getOrder().getId().getValue().toString())
                .restaurantID(orderPaidEvent.getOrder().getRestaurantID().getValue().toString())
                .restaurantOrderStatus(RestaurantOrderStatus.PAID.name())
                .products(orderPaidEvent.getOrder().getOrderItems().stream().map(orderItem ->
                        OrderApprovalEventProduct.builder()
                                .id(orderItem.getProduct().getId().getValue().toString())
                                .quantity(orderItem.getQuantity())
                                .build()).collect(Collectors.toList()))
                .price(orderPaidEvent.getOrder().getPrice().getAmount())
                .createdAt(orderPaidEvent.getCreatedAt())
                .build();
    }

    public Customer customerModelToCustomer(CustomerModel customerModel) {
        return Customer.builder()
                .customerID(new CustomerID(UUID.fromString(customerModel.getId())))
                .username(customerModel.getUsername())
                .firstName(customerModel.getFirstName())
                .lastName(customerModel.getLastName())
                .build();
    }

    private StreetAddress orderAddressToStreetAddress(@NotNull OrderAddress orderAddress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddress.getStreet(),
                orderAddress.getPostalCode(),
                orderAddress.getCity()
        );
    }

    private List<OrderItem> orderItemsDtoToOrderItemsEntities(List<ru.avalc.ordering.application.dto.create.OrderItem> items) {
        return items.stream()
                .map(item ->
                        OrderItem.builder()
                                .product(Product.builder().productID(new ProductID(item.getProductID())).build())
                                .price(new Money(item.getPrice()))
                                .quantity(item.getQuantity())
                                .subTotal(new Money(item.getSubTotal()))
                                .build())
                .collect(Collectors.toList());
    }
}
