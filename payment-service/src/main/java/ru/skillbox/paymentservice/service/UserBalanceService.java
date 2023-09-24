package ru.skillbox.paymentservice.service;

import ru.skillbox.paymentservice.domain.UserBalance;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface UserBalanceService {

    Optional<UserBalance> getMyBalance(HttpServletRequest request);

    Optional<UserBalance> increaseUserBalance(Double amount, HttpServletRequest request);

    Optional<UserBalance> decreaseUserBalance(Double amount, HttpServletRequest request);
}
