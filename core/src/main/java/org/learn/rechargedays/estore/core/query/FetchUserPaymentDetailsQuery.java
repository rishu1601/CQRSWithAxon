package org.learn.rechargedays.estore.core.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FetchUserPaymentDetailsQuery {
    private String userId;
}
