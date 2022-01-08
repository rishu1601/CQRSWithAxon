package org.learn.rechargedays.estore.orderservice.core.events;

import lombok.Value;
import org.learn.rechargedays.estore.orderservice.core.data.OrderStatus;

@Value
public class OrderApprovedEvent {
    private final String orderId;
    private final OrderStatus orderStatus = OrderStatus.APPROVED;
}
