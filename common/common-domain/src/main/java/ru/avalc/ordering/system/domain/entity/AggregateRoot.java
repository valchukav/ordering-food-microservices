package ru.avalc.ordering.system.domain.entity;

/**
 * @author Alexei Valchuk, 06.09.2023, email: a.valchukav@gmail.com
 */

public abstract class AggregateRoot<ID> extends BaseEntity<ID> {

    public AggregateRoot(ID id) {
        super(id);
    }
}
