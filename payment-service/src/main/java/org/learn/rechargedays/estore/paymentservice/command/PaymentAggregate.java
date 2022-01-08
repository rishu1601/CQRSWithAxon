package org.learn.rechargedays.estore.paymentservice.command;


import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.h2.util.StringUtils;
import org.learn.rechargedays.estore.core.commands.ProcessPaymentCommand;
import org.learn.rechargedays.estore.core.events.PaymentProcessedEvent;

@Aggregate
@NoArgsConstructor
public class PaymentAggregate {

    private String orderId;

    @AggregateIdentifier
    private String paymentId;

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand processPaymentCommand) {
        checkValidString(processPaymentCommand.getOrderId());
        checkValidString(processPaymentCommand.getPaymentId());
        PaymentProcessedEvent paymentProcessedEvent = PaymentProcessedEvent.builder()
                .paymentId(processPaymentCommand.getPaymentId())
                .orderId(processPaymentCommand.getOrderId())
                .build();
        AggregateLifecycle.apply(paymentProcessedEvent);
    }

    private void checkValidString(String param) {
        if (StringUtils.isNullOrEmpty(param))
            throw new IllegalArgumentException("Invalid value supplied");
    }

    @EventSourcingHandler
    public void on(ProcessPaymentCommand processPaymentCommand) {
        this.orderId = processPaymentCommand.getOrderId();
        this.paymentId = processPaymentCommand.getPaymentId();
    }

}
