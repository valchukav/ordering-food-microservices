package ru.avalc.ordering.service.domain.dto.create;

import lombok.AllArgsConstructor;
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
@Builder
@AllArgsConstructor
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
}
