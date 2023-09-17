package ru.avalc.ordering.saga;

/**
 * @author Alexei Valchuk, 15.09.2023, email: a.valchukav@gmail.com
 */

public interface SagaStep<T> {

    void process(T data);

    void rollback(T data);
}
