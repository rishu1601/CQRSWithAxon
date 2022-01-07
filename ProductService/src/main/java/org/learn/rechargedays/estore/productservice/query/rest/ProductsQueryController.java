package org.learn.rechargedays.estore.productservice.query.rest;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.learn.rechargedays.estore.productservice.query.FindProductsQuery;
import org.learn.rechargedays.estore.productservice.query.ProductRestDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsQueryController {
    private final QueryGateway queryGateway;

    ProductsQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public List<ProductRestDTO> getProducts() {
        FindProductsQuery findProductsQuery = new FindProductsQuery();
        return queryGateway.query(findProductsQuery,
                                  ResponseTypes.multipleInstancesOf((ProductRestDTO.class)))
                .join();
    }
}
