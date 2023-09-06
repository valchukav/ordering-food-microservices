package ru.avalc.ordering.system.domain.valueobject;

import lombok.Getter;

import java.util.Objects;

/**
 * @author Alexei Valchuk, 06.09.2023, email: a.valchukav@gmail.com
 */

@Getter
public abstract class BaseID<T> {

    private final T value;

    protected BaseID(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseID<?> baseID)) return false;
        return Objects.equals(getValue(), baseID.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
