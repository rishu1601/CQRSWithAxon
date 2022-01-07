package org.learn.rechargedays.estore.productservice.query;

import org.axonframework.queryhandling.QueryHandler;
import org.learn.rechargedays.estore.productservice.core.data.ProductEntity;
import org.learn.rechargedays.estore.productservice.core.data.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductsQueryHandler {
    private final ProductRepository productRepository;

    ProductsQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @QueryHandler
    public List<ProductRestDTO> findProducts(FindProductsQuery findProductsQuery) {
        List<ProductRestDTO> productRestList = new ArrayList<>();
        List<ProductEntity> storedProducts = productRepository.findAll();
        for (ProductEntity product : storedProducts) {
            ProductRestDTO productRestDTO = new ProductRestDTO();
            BeanUtils.copyProperties(product, productRestDTO);
            productRestList.add(productRestDTO);
        }
        return productRestList;
    }
}
