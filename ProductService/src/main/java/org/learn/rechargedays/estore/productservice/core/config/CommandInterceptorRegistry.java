package org.learn.rechargedays.estore.productservice.core.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.learn.rechargedays.estore.productservice.command.interceptors.CreateProductCommandInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CommandInterceptorRegistry {

    private final ApplicationContext applicationContext;
    private final CommandGateway commandGateway;

    @Bean
    public void registerCreateProductCommandInterceptor() {
        commandGateway.registerDispatchInterceptor(applicationContext.getBean(CreateProductCommandInterceptor.class));
    }
}
