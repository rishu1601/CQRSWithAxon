package org.learn.rechargedays.estore.orderservice.saga;

import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.learn.rechargedays.estore.core.commands.CancelProductReservationCommand;
import org.learn.rechargedays.estore.core.commands.ProcessPaymentCommand;
import org.learn.rechargedays.estore.core.commands.ReserveProductCommand;
import org.learn.rechargedays.estore.core.data.UserDetails;
import org.learn.rechargedays.estore.core.events.PaymentProcessedEvent;
import org.learn.rechargedays.estore.core.events.ProductReservationCancelEvent;
import org.learn.rechargedays.estore.core.events.ProductReservedEvent;
import org.learn.rechargedays.estore.core.query.FetchUserPaymentDetailsQuery;
import org.learn.rechargedays.estore.orderservice.command.ApproveOrderCommand;
import org.learn.rechargedays.estore.orderservice.command.RejectOrderCommand;
import org.learn.rechargedays.estore.orderservice.core.data.OrderSummary;
import org.learn.rechargedays.estore.orderservice.core.events.OrderApprovedEvent;
import org.learn.rechargedays.estore.orderservice.core.events.OrderCreatedEvent;
import org.learn.rechargedays.estore.orderservice.core.events.OrderRejectedEvent;
import org.learn.rechargedays.estore.orderservice.query.FindOrderQuery;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
@Slf4j
@AllArgsConstructor
public class OrderSaga {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final QueryUpdateEmitter queryUpdateEmitter;

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
        UserDetails userPaymentDetails = null;
        try {
            userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery,
                                                    ResponseTypes.instanceOf(UserDetails.class)).join();
        } catch (Exception ex) {
            cancelProductReservation(productReservedEvent, ex.getMessage());
            log.error("UserDetails cannot be fetched");
        }
        if (userPaymentDetails == null) {
            cancelProductReservation(productReservedEvent, "userPaymentDetails null");
            return;
        }
        log.info("UserDetails fetched successfully for userId: {}", productReservedEvent.getUserId());
        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .paymentId(UUID.randomUUID().toString())
                .paymentDetails(userPaymentDetails.getPaymentDetails())
                .orderId(productReservedEvent.getOrderId())
                .build();
        String result = null;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            cancelProductReservation(productReservedEvent, e.getMessage());
            log.error("ProcessPaymentCommand Failed", e);
        }
        if (result == null) {
            cancelProductReservation(productReservedEvent, "result is null");
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent) {
        ApproveOrderCommand approveOrderCommand =
                new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
        commandGateway.send(approveOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent orderApprovedEvent) {
        log.info("Order has been approved, orderId:{}", orderApprovedEvent.getOrderId());
        queryUpdateEmitter.emit(FindOrderQuery.class, query -> true,
                                new OrderSummary(orderApprovedEvent.getOrderId(), orderApprovedEvent.getOrderStatus()
                                        , ""));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelEvent productReservationCancelEvent) {
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(productReservationCancelEvent.getOrderId(),
                                                                       productReservationCancelEvent.getReason());
        commandGateway.send(rejectOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent orderRejectedEvent) {
        queryUpdateEmitter.emit(FindOrderQuery.class, query -> true,
                                new OrderSummary(orderRejectedEvent.getOrderId(),
                                                 orderRejectedEvent.getOrderStatus(),
                                                 orderRejectedEvent.getReason()));
    }


    private void cancelProductReservation(ProductReservedEvent productReservedEvent, String errorReason) {
        CancelProductReservationCommand cancelProductReservationCommand =
                CancelProductReservationCommand.builder()
                        .orderId(productReservedEvent.getOrderId())
                        .productId(productReservedEvent.getProductId())
                        .quantity(productReservedEvent.getQuantity())
                        .userId(productReservedEvent.getUserId())
                        .reason(errorReason)
                        .build();
        commandGateway.send(cancelProductReservationCommand);
    }
}
