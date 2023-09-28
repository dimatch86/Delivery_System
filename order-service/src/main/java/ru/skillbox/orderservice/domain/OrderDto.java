package ru.skillbox.orderservice.domain;

import lombok.Data;
import lombok.AllArgsConstructor;
import ru.skillbox.dto.ProductDetailsDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {

    @NotBlank
    private String description;

    @NotBlank
    private String departureAddress;

    @NotBlank
    private String destinationAddress;

    @NotNull
    private List<ProductDetailsDto> productDetails;
}
