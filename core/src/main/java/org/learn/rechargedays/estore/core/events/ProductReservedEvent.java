package org.learn.rechargedays.estore.core.events;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class ProductReservedEvent {
    @TargetAggregateIdentifier
    private final String productId;
    private final String orderId;
    private final String userId;
    private final Integer quantity;
}
