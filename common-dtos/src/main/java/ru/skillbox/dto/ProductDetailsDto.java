package ru.skillbox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsDto {

    @NotBlank
    private String name;
    @NotNull
    @Min(value = 0)
    private Double cost;
    @NotNull
    @Min(value = 0)
    private Integer amount;
}
