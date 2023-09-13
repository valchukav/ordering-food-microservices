package ru.avalc.ordering.order.service.dataaccess.order.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Alexei Valchuk, 11.09.2023, email: a.valchukav@gmail.com
 */

@Table(name = "orders")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    public static final String FAILURE_MESSAGE_DELIMITER = "; ";

    @Id
    private UUID id;
    private UUID customerID;
    private UUID restaurantID;
    private UUID trackingID;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private String failureMessages;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderAddressEntity orderAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> items;

    public static OrderEntityBuilder builder() {
        return new OrderEntityBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderEntity that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public static class OrderEntityBuilder {

        private UUID id;
        private UUID customerID;
        private UUID restaurantID;
        private UUID trackingID;
        private BigDecimal price;
        private OrderStatus orderStatus;
        private String failureMessages;
        private OrderAddressEntity orderAddress;
        private List<OrderItemEntity> items;

        OrderEntityBuilder() {
        }

        public OrderEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public OrderEntityBuilder customerID(UUID customerID) {
            this.customerID = customerID;
            return this;
        }

        public OrderEntityBuilder restaurantID(UUID restaurantID) {
            this.restaurantID = restaurantID;
            return this;
        }

        public OrderEntityBuilder trackingID(UUID trackingID) {
            this.trackingID = trackingID;
            return this;
        }

        public OrderEntityBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public OrderEntityBuilder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderEntityBuilder failureMessages(String failureMessages) {
            this.failureMessages = failureMessages;
            return this;
        }

        public OrderEntityBuilder orderAddress(OrderAddressEntity orderAddress) {
            this.orderAddress = orderAddress;
            return this;
        }

        public OrderEntityBuilder items(List<OrderItemEntity> items) {
            this.items = items;
            return this;
        }

        public OrderEntity build() {
            return new OrderEntity(id, customerID, restaurantID, trackingID, price, orderStatus, failureMessages, orderAddress, items);
        }

        public String toString() {
            return "OrderEntity.OrderEntityBuilder(id=" + this.id + ", customerID=" + this.customerID + ", restaurantID=" + this.restaurantID + ", trackingID=" + this.trackingID + ", price=" + this.price + ", orderStatus=" + this.orderStatus + ", failureMessages=" + this.failureMessages + ", orderAddress=" + this.orderAddress + ", items=" + this.items + ")";
        }
    }
}
