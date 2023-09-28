package ru.skillbox.inventorysrvice.domain.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class QuantityDto {

    @Min(value = 0, message = "Quantity of product must be greater then 0")
    @NotNull
    private int quantity;
}
