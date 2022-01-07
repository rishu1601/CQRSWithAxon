package org.learn.rechargedays.estore.productservice.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.learn.rechargedays.estore.productservice.core.data.ProductEntity;
import org.learn.rechargedays.estore.productservice.core.data.ProductRepository;
import org.learn.rechargedays.estore.productservice.core.events.ProductCreatedEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {

    private final ProductRepository productRepository;

    ProductEventsHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handleException(Exception ex) throws Exception {
        throw ex;
    }
    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) throws Exception {
        ProductEntity product = new ProductEntity();
        BeanUtils.copyProperties(productCreatedEvent, product);

        productRepository.save(product);
    }
}
