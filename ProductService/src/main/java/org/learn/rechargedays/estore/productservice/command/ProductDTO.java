package org.learn.rechargedays.estore.productservice.command;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductDTO {

    @NotNull
    @NotBlank(message = "title cannot be blank")
    private String title;

    @Min(value = 1, message = "price cannot be lower than 1")
    private BigDecimal price;

    @Min(value = 1, message = "quantity cannot be lower than 1")
    @Max(value = 5, message = "quantity cannot be greater than 5")
    private int quantity;
}
