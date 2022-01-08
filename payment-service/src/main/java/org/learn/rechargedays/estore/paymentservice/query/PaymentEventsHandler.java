package org.learn.rechargedays.estore.paymentservice.query;

import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.learn.rechargedays.estore.core.events.PaymentProcessedEvent;
import org.learn.rechargedays.estore.paymentservice.core.data.PaymentEntity;
import org.learn.rechargedays.estore.paymentservice.core.data.PaymentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaymentEventsHandler {
    private final PaymentRepository paymentRepository;

    @EventHandler
    public void handle(PaymentProcessedEvent paymentProcessedEvent) {
        PaymentEntity paymentEntity = new PaymentEntity();
        BeanUtils.copyProperties(paymentProcessedEvent, paymentEntity);
        paymentRepository.save(paymentEntity);
    }
}
