package ru.skillbox.inventorysrvice.domain.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductDto {

    @NotBlank
    private String name;
    @NotNull
    @Min(value = 0, message = "Quantity of product must be greater then 0")
    private Integer quantity;


}
