package ru.avalc.ordering.order.service.dataaccess.outbox.payment.entity;

import lombok.*;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.saga.SagaStatus;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 18.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_outbox")
@Entity
public class PaymentOutboxEntity {

    @Id
    private UUID id;

    @Column(name = "saga_id")
    private UUID sagaID;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "processed_at")
    private ZonedDateTime processedAt;

    private String type;
    private String payload;

    @Column(name = "saga_status")
    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "outbox_status")
    @Enumerated(EnumType.STRING)
    private OutboxStatus outboxStatus;

    @Version
    private int version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentOutboxEntity that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
