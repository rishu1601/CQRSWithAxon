package org.learn.rechargedays.estore.orderservice.core.data;

import lombok.Value;

@Value
public class OrderSummary {
    String orderId;
    OrderStatus orderStatus;
    String message;
}
