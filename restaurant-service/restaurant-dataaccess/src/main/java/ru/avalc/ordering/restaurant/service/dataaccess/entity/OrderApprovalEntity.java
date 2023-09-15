package ru.avalc.ordering.restaurant.service.dataaccess.entity;

import lombok.*;
import ru.avalc.ordering.system.domain.valueobject.OrderApprovalStatus;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 15.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_approval", schema = "restaurant")
@Entity
public class OrderApprovalEntity {

    @Id
    private UUID id;

    @Column(name = "restaurant_id")
    private UUID restaurantID;

    @Column(name = "order_id")
    private UUID orderID;

    @Enumerated(EnumType.STRING)
    private OrderApprovalStatus status;
}
