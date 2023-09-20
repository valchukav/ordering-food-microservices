package ru.avalc.ordering.restaurant.service.dataaccess.outbox.entity;

import lombok.*;
import ru.avalc.ordering.outbox.OutboxStatus;
import ru.avalc.ordering.system.domain.valueobject.OrderApprovalStatus;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 19.09.2023, email: a.valchukav@gmail.com
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_outbox")
@Entity
public class OrderOutboxEntity {

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

    @Column(name = "outbox_status")
    @Enumerated(EnumType.STRING)
    private OutboxStatus outboxStatus;

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private OrderApprovalStatus approvalStatus;
    private int version;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderOutboxEntity that = (OrderOutboxEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
