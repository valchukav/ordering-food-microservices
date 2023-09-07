package ru.avalc.ordering.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 07.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Builder
@AllArgsConstructor
public class OrderItem {

    @NotNull
    private final UUID productID;

    @NotNull
    private final Integer quantity;

    @NotNull
    private final BigDecimal price;

    @NotNull
    private final BigDecimal subTotal;
}
