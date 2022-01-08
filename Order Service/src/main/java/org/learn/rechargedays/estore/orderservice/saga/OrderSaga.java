package org.learn.rechargedays.estore.orderservice.saga;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.learn.rechargedays.estore.core.commands.ProcessPaymentCommand;
import org.learn.rechargedays.estore.core.commands.ReserveProductCommand;
import org.learn.rechargedays.estore.core.data.UserDetails;
import org.learn.rechargedays.estore.core.events.ProductReservedEvent;
import org.learn.rechargedays.estore.core.query.FetchUserPaymentDetailsQuery;
import org.learn.rechargedays.estore.orderservice.core.events.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
@Slf4j
@AllArgsConstructor
public class OrderSaga {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .quantity(orderCreatedEvent.getQuantity())
                .userId(orderCreatedEvent.getUserId())
                .build();
        log.info("ReserveProductCommand issued: {}", reserveProductCommand);
        commandGateway.send(reserveProductCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                // Start compensating transaction
                log.error("Cannot process reserveProductCommand", commandResultMessage.exceptionResult());
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent) {
        log.info("ProductReservedEvent called for productId:{}, orderId:{}", productReservedEvent.getProductId(),
                 productReservedEvent.getOrderId());
        FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery =
                new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
        UserDetails userDetails = null;
        try {
            userDetails = queryGateway.query(fetchUserPaymentDetailsQuery,
                                             ResponseTypes.instanceOf(UserDetails.class)).join();
        } catch (Exception ex) {
            log.error("UserDetails cannot be fetched");
        }
        if(userDetails == null) {
            //compensate the transaction
            return;
        }
        log.info("UserDetails fetched successfully for userId: {}", productReservedEvent.getUserId());
        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .paymentId(UUID.randomUUID().toString())
                .paymentDetails(userDetails.getPaymentDetails())
                .orderId(productReservedEvent.getOrderId())
                .build();
        String result = null;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            //Compensating Transaction
            log.error("ProcessPaymentCommand Failed", e);
        }
        if(result == null) {
            //Compensating Transaction
        }



    }


}
