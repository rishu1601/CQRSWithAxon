package org.learn.rechargedays.estore.core.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReservationCancelEvent {
    private String productId;

    private String orderId;

    private int quantity;

    private String userId;

    private String reason;
}
