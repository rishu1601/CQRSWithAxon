package org.learn.rechargedays.estore.productservice.command.rest;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.learn.rechargedays.estore.productservice.command.CreateProductCommand;
import org.learn.rechargedays.estore.productservice.command.ProductDTO;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductCommandController {

    private final Environment environment;
    private final CommandGateway commandGateway;

    ProductCommandController(Environment environment, CommandGateway commandGateway) {
        this.environment = environment;
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createProduct(@Valid @RequestBody ProductDTO productDTO) {
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .price(productDTO.getPrice())
                .quantity(productDTO.getQuantity())
                .title(productDTO.getTitle())
                .productId(UUID.randomUUID().toString())
                .build();

        return commandGateway.sendAndWait(createProductCommand);
    }

//    @GetMapping
//    public String getProduct() {
//        return "HTTP GET Handled " + environment.getProperty("local.server.port");
//    }
//
//    @PutMapping
//    public String updateProduct() {
//        return "HTTP PUT Handled";
//    }
//
//    @DeleteMapping
//    public String deleteProduct() {
//        return "HTTP DELETE Handled";
//    }

}
