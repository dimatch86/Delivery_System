package ru.skillbox.dto;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@ToString
public class OrderKafkaDto {

    private Long orderId;

    private String status;

    private String creationTime;

    private String modifiedTime;

    private String userName;

    private Double orderPrice;
    private String departureAddress;
    private String destinationAddress;

    private List<ProductDetailsDto> productDetails;

    private String userToken;
}
