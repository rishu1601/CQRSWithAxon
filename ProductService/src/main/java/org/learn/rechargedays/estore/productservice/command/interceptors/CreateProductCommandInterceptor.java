package org.learn.rechargedays.estore.productservice.command.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.learn.rechargedays.estore.productservice.command.CreateProductCommand;
import org.learn.rechargedays.estore.productservice.core.data.ProductLookupEntity;
import org.learn.rechargedays.estore.productservice.core.data.ProductLookupRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
@Slf4j
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookupRepository productLookupRepository;

    CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> list) {
        return (index, command) -> {
            log.info("Intercepted command: {}", command.getPayloadType());
            if (CreateProductCommand.class.equals(command.getPayloadType())) {
                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
                ProductLookupEntity productLookupEntity =
                        productLookupRepository.findByProductIdOrTitle(createProductCommand.getProductId(),
                                                                       createProductCommand.getTitle());
                if (productLookupEntity != null) {
                    throw new IllegalStateException(String.format("Product with %s title or %s productId alreadyExists",
                                                                  createProductCommand.getTitle(),
                                                                  createProductCommand.getProductId()));
                }
            }
            return command;
        };
    }
}
