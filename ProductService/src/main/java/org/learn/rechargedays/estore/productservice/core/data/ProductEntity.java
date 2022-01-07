package org.learn.rechargedays.estore.productservice.core.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
public class ProductEntity implements Serializable {

    @Id
    @Column(unique = true)
    private String productId;

    @Column(unique = true)
    private String title;

    private BigDecimal price;

    private int quantity;

}
