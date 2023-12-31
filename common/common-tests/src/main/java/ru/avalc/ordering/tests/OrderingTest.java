package ru.avalc.ordering.tests;

import ru.avalc.ordering.application.dto.create.CreateOrderCommand;
import ru.avalc.ordering.application.dto.create.OrderAddress;
import ru.avalc.ordering.application.dto.create.OrderItem;
import ru.avalc.ordering.domain.entity.Customer;
import ru.avalc.ordering.domain.entity.Order;
import ru.avalc.ordering.domain.entity.Product;
import ru.avalc.ordering.domain.entity.Restaurant;
import ru.avalc.ordering.system.domain.valueobject.CustomerID;
import ru.avalc.ordering.system.domain.valueobject.Money;
import ru.avalc.ordering.system.domain.valueobject.ProductID;
import ru.avalc.ordering.system.domain.valueobject.RestaurantID;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 11.09.2023, email: a.valchukav@gmail.com
 */

public abstract class OrderingTest {

    protected static CreateOrderCommand createOrderCommand;
    protected static CreateOrderCommand createOrderCommandWrongPrice;
    protected static CreateOrderCommand createOrderCommandWrongProductPrice;
    protected static Restaurant restaurant;
    protected static Customer customer;
    protected static ru.avalc.customer.service.domain.entity.Customer fullCustomer;
    protected static Order order;
    protected static final UUID CUSTOMER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb41");
    protected static final UUID RESTAURANT_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb45");
    protected static final UUID SAGA_ID = UUID.fromString("15a497c1-0f4b-4eff-b9f4-c402c8c07afa");
    protected static final UUID PRODUCT_ID_1 = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb48");
    protected static final UUID PRODUCT_ID_2 = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb48");
    protected static final UUID ORDER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb17");
    protected static final UUID PAYMENT_ID = UUID.randomUUID();
    protected static final BigDecimal PRICE = BigDecimal.valueOf(200);

    static {
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

        customer = Customer.builder().customerID(new CustomerID(CUSTOMER_ID)).build();

        fullCustomer = ru.avalc.customer.service.domain.entity.Customer.builder()
                .customerID(new CustomerID(CUSTOMER_ID))
                .username("customer")
                .firstName("customer")
                .lastName("customer")
                .build();

        restaurant = Restaurant.builder()
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
    }
}
