package ru.avalc.ordering.payment.service.dataaccess.payment.entity;

import lombok.*;
import ru.avalc.ordering.system.domain.valueobject.PaymentStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 14.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
@Entity
public class PaymentEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id")
    private UUID customerID;

    @Column(name = "order_id")
    private UUID orderID;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentEntity that = (PaymentEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
