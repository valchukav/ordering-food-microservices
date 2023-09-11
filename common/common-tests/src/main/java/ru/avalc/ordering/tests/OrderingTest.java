package ru.avalc.ordering.tests;

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

    protected Restaurant restaurant;
    protected Customer customer;
    protected Order order;
    protected final UUID CUSTOMER_ID = UUID.randomUUID();
    protected final UUID RESTAURANT_ID = UUID.randomUUID();
    protected final UUID PRODUCT_ID_1 = UUID.randomUUID();
    protected final UUID PRODUCT_ID_2 = UUID.randomUUID();
    protected final UUID ORDER_ID = UUID.randomUUID();
    protected final BigDecimal PRICE = BigDecimal.valueOf(200);

    public void init() {
        customer = Customer.builder().customerID(new CustomerID(CUSTOMER_ID)).build();

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
