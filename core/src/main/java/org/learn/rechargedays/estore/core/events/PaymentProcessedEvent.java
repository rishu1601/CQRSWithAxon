package org.learn.rechargedays.estore.core.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentProcessedEvent {
    private final String orderId;
    private final String paymentId;
}
