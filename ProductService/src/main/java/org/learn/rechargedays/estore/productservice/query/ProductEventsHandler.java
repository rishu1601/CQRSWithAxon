package org.learn.rechargedays.estore.productservice.query;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.learn.rechargedays.estore.core.events.ProductReservationCancelEvent;
import org.learn.rechargedays.estore.core.events.ProductReservedEvent;
import org.learn.rechargedays.estore.productservice.core.data.ProductEntity;
import org.learn.rechargedays.estore.productservice.core.data.ProductRepository;
import org.learn.rechargedays.estore.productservice.core.events.ProductCreatedEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
@Slf4j
public class ProductEventsHandler {

    private final ProductRepository productRepository;

    ProductEventsHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @ExceptionHandler()
    public void handleException(Exception ex) throws Exception {
        throw ex;
    }

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        ProductEntity product = new ProductEntity();
        BeanUtils.copyProperties(productCreatedEvent, product);

        productRepository.save(product);
    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent) {
        log.info("Processing ProductReservedEvent:{}", productReservedEvent);
        ProductEntity product = productRepository.findByProductId(productReservedEvent.getProductId());
        product.setQuantity(productReservedEvent.getQuantity());
        productRepository.save(product);
    }

    @EventHandler
    public void on(ProductReservationCancelEvent productReservationCancelEvent) {
        ProductEntity product = productRepository.findByProductId(productReservationCancelEvent.getProductId());
        product.setQuantity(productReservationCancelEvent.getQuantity() + product.getQuantity());
        productRepository.save(product);
    }
}
