package ru.skillbox.paymentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.paymentservice.domain.UserBalance;
import ru.skillbox.paymentservice.domain.dto.BalanceDto;
import ru.skillbox.paymentservice.errors.BalanceNotFoundException;
import ru.skillbox.paymentservice.service.UserBalanceService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BalanceController {

    private final UserBalanceService userBalanceService;

    @Operation(summary = "Get balance of current user", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/balance")
    public UserBalance getMyBalance(HttpServletRequest request) {
        return userBalanceService.getMyBalance(request)
                .orElseThrow(BalanceNotFoundException::new);
    }

    @Operation(summary = "Increase user balance", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add")
    public ResponseEntity<UserBalance> addMoney(@RequestBody @Valid BalanceDto balanceDto, HttpServletRequest request) {

        return userBalanceService.increaseUserBalance(balanceDto.getAmount(), request)
                .map(userBalance -> ResponseEntity.status(HttpStatus.OK).body(userBalance))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build());

    }

    @Operation(summary = "Decrease user balance", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/take")
    public ResponseEntity<UserBalance> takeMoney(@RequestBody BalanceDto balanceDto, HttpServletRequest request) {

        return userBalanceService.decreaseUserBalance(balanceDto.getAmount(), request)
                .map(userBalance -> ResponseEntity.status(HttpStatus.OK).body(userBalance))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build());

    }
}
