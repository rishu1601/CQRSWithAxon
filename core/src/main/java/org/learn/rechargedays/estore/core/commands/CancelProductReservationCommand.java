package org.learn.rechargedays.estore.core.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CancelProductReservationCommand {
    @TargetAggregateIdentifier
    private String productId;

    private String orderId;

    private int quantity;

    private String userId;

    private String reason;
}
