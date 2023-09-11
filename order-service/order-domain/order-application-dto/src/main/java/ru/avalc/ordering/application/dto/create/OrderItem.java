package ru.avalc.ordering.application.dto.create;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public class OrderItem {

    @NotNull
    private final UUID productID;

    @NotNull
    private final Integer quantity;

    @NotNull
    private final BigDecimal price;

    @NotNull
    private final BigDecimal subTotal;

    @Builder
    private OrderItem(UUID productID, Integer quantity, BigDecimal price, BigDecimal subTotal) {
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = subTotal;
    }
}
