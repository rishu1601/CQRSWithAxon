package org.learn.rechargedays.estore.usersservice.query;

import org.axonframework.queryhandling.QueryHandler;
import org.learn.rechargedays.estore.core.data.PaymentDetails;
import org.learn.rechargedays.estore.core.data.UserDetails;
import org.learn.rechargedays.estore.core.query.FetchUserPaymentDetailsQuery;
import org.springframework.stereotype.Component;

@Component
public class UserEventsHandler {

    @QueryHandler
    public UserDetails findUserDetails(FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery) {
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .name("RISHABH KUMAR")
                .cardNumber("1111222233334444")
                .cvv("111")
                .validUntilMonth(11)
                .validUntilYear(2999)
                .build();

        return UserDetails.builder()
                .paymentDetails(paymentDetails)
                .firstName("RISHABH")
                .lastName("KUMAR")
                .userId(fetchUserPaymentDetailsQuery.getUserId())
                .build();
    }
}
