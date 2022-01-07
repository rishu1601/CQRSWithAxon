package org.learn.rechargedays.estore.productservice.command;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.learn.rechargedays.estore.productservice.core.data.ProductLookupEntity;
import org.learn.rechargedays.estore.productservice.core.data.ProductLookupRepository;
import org.learn.rechargedays.estore.productservice.core.events.ProductCreatedEvent;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
@RequiredArgsConstructor
public class ProductLookupEventsHandler {

    private final ProductLookupRepository productLookupRepository;

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        ProductLookupEntity productLookupEntity = new ProductLookupEntity(productCreatedEvent.getProductId(),
                                                                          productCreatedEvent.getTitle());
        productLookupRepository.save(productLookupEntity);

    }

}
