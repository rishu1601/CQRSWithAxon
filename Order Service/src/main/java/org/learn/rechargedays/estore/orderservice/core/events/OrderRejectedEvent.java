package org.learn.rechargedays.estore.orderservice.core.events;

import lombok.Value;
import org.learn.rechargedays.estore.orderservice.core.data.OrderStatus;

@Value
public class OrderRejectedEvent {
    String orderId;

    String reason;

    OrderStatus orderStatus = OrderStatus.REJECTED;
}