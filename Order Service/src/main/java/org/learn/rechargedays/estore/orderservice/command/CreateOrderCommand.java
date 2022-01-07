package org.learn.rechargedays.estore.orderservice.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.learn.rechargedays.estore.orderservice.core.data.OrderStatus;

@Data
@Builder
public class CreateOrderCommand {
    @TargetAggregateIdentifier
    private final String orderId;
    private final String userId;
    private final String productId;
    private final int quantity;
    private final String addressId;
    private final OrderStatus orderStatus;
}
