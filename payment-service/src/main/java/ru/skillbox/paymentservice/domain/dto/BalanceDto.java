package ru.skillbox.paymentservice.domain.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class BalanceDto {

    @Min(value = 0, message = "Amount must be equals or greater than 0")
    @NotNull
    private Double amount;
}
