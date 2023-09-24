package ru.skillbox.orderservice.domain;

import lombok.Data;
import lombok.AllArgsConstructor;
import ru.skillbox.dto.ProductDetailsDto;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {

    private String description;

    private String departureAddress;

    private String destinationAddress;

    private List<ProductDetailsDto> productDetailsDto;
}
