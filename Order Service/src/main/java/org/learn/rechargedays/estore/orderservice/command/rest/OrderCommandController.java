package org.learn.rechargedays.estore.orderservice.command.rest;

import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.learn.rechargedays.estore.orderservice.command.CreateOrderCommand;
import org.learn.rechargedays.estore.orderservice.core.data.OrderStatus;
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

    @PostMapping
    public String createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        CreateOrderCommand createOrderCommand =
                CreateOrderCommand.builder()
                        .orderId(UUID.randomUUID().toString())
                        .orderStatus(OrderStatus.CREATED)
                        .addressId(orderRequest.getAddressId())
                        .productId(orderRequest.getProductId())
                        .quantity(orderRequest.getQuantity())
                        .userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
                        .build();
        return commandGateway.sendAndWait(createOrderCommand);
    }
}
