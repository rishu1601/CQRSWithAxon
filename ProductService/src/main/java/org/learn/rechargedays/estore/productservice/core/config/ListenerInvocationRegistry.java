package org.learn.rechargedays.estore.productservice.core.config;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.EventProcessingConfigurer;
import org.learn.rechargedays.estore.productservice.core.errorhandling.ProductServiceEventsErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ListenerInvocationRegistry {

    private final EventProcessingConfigurer eventProcessingConfigurer;

    @Bean
    public void configure() {
        eventProcessingConfigurer.registerListenerInvocationErrorHandler("product-group",
                                                                         configuration -> new ProductServiceEventsErrorHandler());
    }
}
