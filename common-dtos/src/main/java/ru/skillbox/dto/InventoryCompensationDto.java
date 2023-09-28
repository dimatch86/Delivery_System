package ru.skillbox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryCompensationDto {

    private String userName;

    private Double paymentAmount;

    private List<ProductDetailsDto> products;
}
