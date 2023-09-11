package ru.avalc.ordering.application.dto.create;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class CreateOrderCommand {

    @NotNull
    private final UUID customerID;

    @NotNull
    private final UUID restaurantID;

    @NotNull
    private final BigDecimal price;

    @NotNull
    private final List<OrderItem> items;

    @NotNull
    private final OrderAddress address;

    @Builder
    private CreateOrderCommand(UUID customerID, UUID restaurantID, BigDecimal price, List<OrderItem> items, OrderAddress address) {
        this.customerID = customerID;
        this.restaurantID = restaurantID;
        this.price = price;
        this.items = items;
        this.address = address;
    }
}
