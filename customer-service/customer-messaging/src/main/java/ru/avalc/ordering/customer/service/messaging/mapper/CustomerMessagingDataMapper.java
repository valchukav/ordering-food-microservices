package ru.avalc.ordering.customer.service.messaging.mapper;

import org.springframework.stereotype.Component;
import ru.avalc.customer.service.domain.event.CustomerCreatedEvent;
import ru.avalc.ordering.kafka.order.avro.model.CustomerAvroModel;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Component
public class CustomerMessagingDataMapper {

    public CustomerAvroModel paymentResponseAvroModelToPaymentResponse(CustomerCreatedEvent customerCreatedEvent) {
        return CustomerAvroModel.newBuilder()
                .setId(customerCreatedEvent.getCustomer().getId().getValue().toString())
                .setUsername(customerCreatedEvent.getCustomer().getUsername())
                .setFirstName(customerCreatedEvent.getCustomer().getFirstName())
                .setLastName(customerCreatedEvent.getCustomer().getLastName())
                .build();
    }
}
