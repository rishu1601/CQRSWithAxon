package org.learn.rechargedays.estore.core.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetails {
    private final String firstName;
    private final String lastName;
    private final String userId;
    private final PaymentDetails paymentDetails;
}
