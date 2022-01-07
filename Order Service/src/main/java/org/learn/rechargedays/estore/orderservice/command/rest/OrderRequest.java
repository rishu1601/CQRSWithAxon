package org.learn.rechargedays.estore.orderservice.command.rest;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderRequest {

    @NotNull
    @NotBlank
    private String productId;

    @Min(value = 1)
    @Max(value = 5)
    private int quantity;

    @NotNull
    @NotBlank
    private String addressId;

}
