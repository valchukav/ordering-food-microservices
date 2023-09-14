package ru.avalc.ordering.payment.service.dataaccess.credithistory.entity;

import lombok.*;
import ru.avalc.ordering.payment.service.domain.valueobject.TransactionType;

import javax.persistence.*;
import java.math.BigDecimal;
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
@Table(name = "credit_history")
@Entity
public class CreditHistoryEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id")
    private UUID customerID;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditHistoryEntity that = (CreditHistoryEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
