package ru.avalc.ordering.service.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.avalc.ordering.domain.entity.Customer;
import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.entity.Product;
import ru.avalc.ordering.domain.entity.Restaurant;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.service.domain.dto.create.CreateOrderCommand;
import ru.avalc.ordering.service.domain.dto.create.CreateOrderResponse;
import ru.avalc.ordering.service.domain.dto.create.OrderAddress;
import ru.avalc.ordering.service.domain.dto.create.OrderItem;
import ru.avalc.ordering.service.domain.mapper.OrderDataMapper;
import ru.avalc.ordering.service.domain.ports.input.service.OrderApplicationService;
import ru.avalc.ordering.service.domain.ports.output.repository.CustomerRepository;
import ru.avalc.ordering.service.domain.ports.output.repository.OrderRepository;
import ru.avalc.ordering.service.domain.ports.output.repository.RestaurantRepository;
import ru.avalc.ordering.system.domain.valueobject.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * @author Alexei Valchuk, 09.09.2023, email: a.valchukav@gmail.com
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderConfigurationTest.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.randomUUID();
    private final UUID RESTAURANT_ID = UUID.randomUUID();
    private final UUID PRODUCT_ID_1 = UUID.randomUUID();
    private final UUID PRODUCT_ID_2 = UUID.randomUUID();
    private final UUID ORDER_ID = UUID.randomUUID();
    private final BigDecimal PRICE = BigDecimal.valueOf(200);

    @BeforeAll
    public void init() {
        createOrderCommand = CreateOrderCommand.builder()
                .customerID(CUSTOMER_ID)
                .restaurantID(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("st_1")
                        .postalCode("1")
                        .city("city_1")
                        .build())
                .price(PRICE)
                .items(List.of(OrderItem.builder()
                                .productID(PRODUCT_ID_1)
                                .quantity(1)
                                .price(BigDecimal.valueOf(50))
                                .subTotal(BigDecimal.valueOf(50))
                                .build(),
                        OrderItem.builder()
                                .productID(PRODUCT_ID_2)
                                .quantity(3)
                                .price(BigDecimal.valueOf(50))
                                .subTotal(BigDecimal.valueOf(150))
                                .build()))
                .build();

        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerID(CUSTOMER_ID)
                .restaurantID(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("st_1")
                        .postalCode("1")
                        .city("city_1")
                        .build())
                .price(BigDecimal.valueOf(250))
                .items(List.of(OrderItem.builder()
                                .productID(PRODUCT_ID_1)
                                .quantity(1)
                                .price(BigDecimal.valueOf(50))
                                .subTotal(BigDecimal.valueOf(50))
                                .build(),
                        OrderItem.builder()
                                .productID(PRODUCT_ID_2)
                                .quantity(3)
                                .price(BigDecimal.valueOf(50))
                                .subTotal(BigDecimal.valueOf(150))
                                .build()))
                .build();

        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
                .customerID(CUSTOMER_ID)
                .restaurantID(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("st_1")
                        .postalCode("1")
                        .city("city_1")
                        .build())
                .price(BigDecimal.valueOf(210))
                .items(List.of(OrderItem.builder()
                                .productID(PRODUCT_ID_1)
                                .quantity(1)
                                .price(BigDecimal.valueOf(60))
                                .subTotal(BigDecimal.valueOf(60))
                                .build(),
                        OrderItem.builder()
                                .productID(PRODUCT_ID_2)
                                .quantity(3)
                                .price(BigDecimal.valueOf(50))
                                .subTotal(BigDecimal.valueOf(150))
                                .build()))
                .build();

        Customer customer = Customer.builder().customerID(new CustomerID(CUSTOMER_ID)).build();

        Restaurant restaurant = Restaurant.builder()
                .restaurantID(new RestaurantID(RESTAURANT_ID))
                .active(true)
                .products(List.of(
                        Product.builder()
                                .productID(new ProductID(PRODUCT_ID_1))
                                .name("prod_1")
                                .price(new Money(50))
                                .build(),
                        Product.builder()
                                .productID(new ProductID(PRODUCT_ID_2))
                                .name("prod_2")
                                .price(new Money(50))
                                .build()
                ))
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderID(ORDER_ID));

        when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurant));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
    }

    @Test
    public void testCreateOrder() {
        CreateOrderResponse orderResponse = orderApplicationService.createOrder(createOrderCommand);
        Assertions.assertAll(
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.PENDING),
                () -> assertThat(orderResponse.getMessage()).isNotEmpty(),
                () -> assertThat(orderResponse.getOrderTrackingID()).isNotNull()
        );
    }

    @Test
    public void testCreateOrderWithWrongTotalPrice() {
        OrderDomainException exception = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
        assertThat(exception.getMessage()).contains(List.of("price", PRICE.toString()));
    }

    @Test
    public void testCreateOrderWithWrongProductPrice() {
        OrderDomainException exception = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
        assertThat(exception.getMessage()).contains(List.of("price", "not valid"));
    }

    @Test
    public void testCreateOrderWithPassiveRestaurant() {
        Restaurant restaurant = Restaurant.builder()
                .restaurantID(new RestaurantID(RESTAURANT_ID))
                .active(false)
                .products(List.of(
                        Product.builder()
                                .productID(new ProductID(PRODUCT_ID_1))
                                .name("prod_1")
                                .price(new Money(50))
                                .build(),
                        Product.builder()
                                .productID(new ProductID(PRODUCT_ID_2))
                                .name("prod_2")
                                .price(new Money(50))
                                .build()
                ))
                .build();

        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurant));

        OrderDomainException exception = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommand));

        assertThat(exception.getMessage()).contains("not active");
    }
}
