package org.learn.rechargedays.estore.orderservice.core.events;

import lombok.Data;
import org.learn.rechargedays.estore.orderservice.core.data.OrderStatus;

@Data
public class OrderCreatedEvent {
    private String orderId;

    private String userId;

    private String productId;

    private int quantity;

    private String addressId;

    private OrderStatus orderStatus;
}
