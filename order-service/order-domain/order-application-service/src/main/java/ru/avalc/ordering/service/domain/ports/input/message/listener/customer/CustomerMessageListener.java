package ru.avalc.ordering.service.domain.ports.input.message.listener.customer;

import ru.avalc.ordering.application.dto.message.CustomerModel;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

public interface CustomerMessageListener {

    void customerCreated(CustomerModel customerModel);
}
