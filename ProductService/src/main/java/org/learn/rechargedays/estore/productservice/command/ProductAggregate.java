package org.learn.rechargedays.estore.productservice.command;

import org.apache.commons.lang.StringUtils;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.learn.rechargedays.estore.core.commands.CancelProductReservationCommand;
import org.learn.rechargedays.estore.core.commands.ReserveProductCommand;
import org.learn.rechargedays.estore.core.events.ProductReservationCancelEvent;
import org.learn.rechargedays.estore.core.events.ProductReservedEvent;
import org.learn.rechargedays.estore.productservice.core.events.ProductCreatedEvent;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;

    private String title;

    private BigDecimal price;

    private int quantity;

    public ProductAggregate() {

    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price cannot be zero or negative");
        }
        if (StringUtils.isBlank(createProductCommand.getTitle())) {
            throw new IllegalArgumentException("Product title cannot be blank");
        }

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);

        AggregateLifecycle.apply(productCreatedEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.productId = productCreatedEvent.getProductId();
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
        this.title = productCreatedEvent.getTitle();
    }

    @CommandHandler
    public void on(ReserveProductCommand reserveProductCommand) {
        //quantity is fetched from axon state object
        if (quantity < reserveProductCommand.getQuantity()) {
            throw new IllegalStateException("Insufficient items in stock");
        }

        ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .orderId(reserveProductCommand.getOrderId())
                .userId(reserveProductCommand.getUserId())
                .build();

        AggregateLifecycle.apply(productReservedEvent);
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent) {
        this.quantity -= productReservedEvent.getQuantity();
    }

    @CommandHandler
    public void on(CancelProductReservationCommand command) {
        ProductReservationCancelEvent productReservationCancelEvent = ProductReservationCancelEvent.builder()
                .orderId(command.getOrderId())
                .productId(command.getProductId())
                .userId(command.getUserId())
                .quantity(command.getQuantity())
                .reason(command.getReason())
                .build();
        AggregateLifecycle.apply(productReservationCancelEvent);
    }

    @EventSourcingHandler
    public void handle(ProductReservationCancelEvent productReservationCancelEvent) {
        this.quantity += productReservationCancelEvent.getQuantity();
    }
}
