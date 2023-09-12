package ru.avalc.ordering.order.service.dataaccess.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.avalc.ordering.order.service.dataaccess.customer.entity.CustomerEntity;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 12.09.2023, email: a.valchukav@gmail.com
 */

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {

}
