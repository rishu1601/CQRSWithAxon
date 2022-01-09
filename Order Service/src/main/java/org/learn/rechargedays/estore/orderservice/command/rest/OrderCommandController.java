package org.learn.rechargedays.estore.orderservice.command.rest;

import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.learn.rechargedays.estore.orderservice.command.CreateOrderCommand;
import org.learn.rechargedays.estore.orderservice.core.data.OrderStatus;
import org.learn.rechargedays.estore.orderservice.core.data.OrderSummary;
import org.learn.rechargedays.estore.orderservice.query.FindOrderQuery;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderCommandController {

    private final CommandGateway commandGateway;

    private final QueryGateway queryGateway;

    @PostMapping
    public OrderSummary createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";
        String orderId = UUID.randomUUID().toString();
        CreateOrderCommand createOrderCommand =
                CreateOrderCommand.builder()
                        .orderId(orderId)
                        .orderStatus(OrderStatus.CREATED)
                        .addressId(orderRequest.getAddressId())
                        .productId(orderRequest.getProductId())
                        .quantity(orderRequest.getQuantity())
                        .userId(userId)
                        .build();

        SubscriptionQueryResult<OrderSummary, OrderSummary> result = queryGateway.subscriptionQuery(new FindOrderQuery(orderId),
                                                                                                    ResponseTypes.instanceOf(OrderSummary.class),
                                                                                                    ResponseTypes.instanceOf(OrderSummary.class));
        try {
            commandGateway.sendAndWait(createOrderCommand);
            return result.updates().blockFirst();
        } finally {
            result.close();
        }
    }


}
