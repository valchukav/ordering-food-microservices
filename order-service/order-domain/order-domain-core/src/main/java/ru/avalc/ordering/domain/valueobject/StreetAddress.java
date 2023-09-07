package ru.avalc.ordering.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 06.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@AllArgsConstructor
public class StreetAddress {

    private final UUID id;
    private final String street;
    private final String postalCode;
    private final String city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StreetAddress that)) return false;
        return Objects.equals(getStreet(), that.getStreet()) && Objects.equals(getPostalCode(), that.getPostalCode()) && Objects.equals(getCity(), that.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreet(), getPostalCode(), getCity());
    }
}
