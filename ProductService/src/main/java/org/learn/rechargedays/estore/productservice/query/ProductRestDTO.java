package org.learn.rechargedays.estore.productservice.query;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRestDTO {
    private String productId;

    private String title;

    private BigDecimal price;

    private int quantity;
}
