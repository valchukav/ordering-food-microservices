package ru.avalc.ordering.customer.service.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.avalc.ordering.customer.service.dataaccess.entity.CustomerEntity;

import java.util.UUID;

/**
 * @author Alexei Valchuk, 21.09.2023, email: a.valchukav@gmail.com
 */

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {

}
