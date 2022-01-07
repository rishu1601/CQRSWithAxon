package org.learn.rechargedays.estore.orderservice.core.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private String orderId;

    private String userId;

    private String productId;

    private int quantity;

    private String addressId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
