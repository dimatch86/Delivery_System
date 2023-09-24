package ru.skillbox.orderservice.util;

import lombok.experimental.UtilityClass;
import ru.skillbox.dto.ProductDetailsDto;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.dto.OrderKafkaDto;
import ru.skillbox.orderservice.domain.ProductDetails;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class KafkaOrderDtoMapper {

    public OrderKafkaDto toOrderKafkaDto(Order order) {
        return new OrderKafkaDto(
                order.getId(),
                order.getStatus().toString(),
                order.getCreationTime().toString(),
                order.getModifiedTime().toString(),
                order.getUserName(),
                order.getCost(),
                order.getDepartureAddress(),
                order.getDestinationAddress(),
                toProductDetailsDtoList(order.getProductDetails()),
                ""
        );
    }

    private List<ProductDetailsDto> toProductDetailsDtoList(List<ProductDetails> productDetailsList) {
        List<ProductDetailsDto> productDetailsDtos = new ArrayList<>();
        productDetailsList.forEach(productDetails -> productDetailsDtos.add(new ProductDetailsDto(productDetails.getName(),
                productDetails.getCost(), productDetails.getAmount())));

        return productDetailsDtos;

    }
}
