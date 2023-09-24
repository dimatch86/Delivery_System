package ru.skillbox.paymentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.util.CurrentUserUtils;
import ru.skillbox.paymentservice.domain.UserBalance;
import ru.skillbox.paymentservice.repository.UserBalanceRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBalanceServiceImpl implements UserBalanceService {

    private final UserBalanceRepository userBalanceRepository;

    @Override
    @Transactional
    public Optional<UserBalance> increaseUserBalance(Double amount, HttpServletRequest request) {
        String userName = CurrentUserUtils.getCurrentUser(request);

        UserBalance userBalance = userBalanceRepository.findUserBalanceByUserName(userName);

        if (userBalance != null) {
            userBalance.setBalance(userBalance.getBalance() + amount);
            userBalanceRepository.save(userBalance);
        } else {
            userBalance = userBalanceRepository.save(new UserBalance(userName, amount));
        }

        return Optional.of(userBalance);
    }

    @Override
    @Transactional
    public Optional<UserBalance> decreaseUserBalance(Double amount, HttpServletRequest request) {
        String userName = CurrentUserUtils.getCurrentUser(request);

        UserBalance userBalance = userBalanceRepository.findUserBalanceByUserName(userName);
        if (userBalance != null) {
            userBalance.setBalance(amount > userBalance.getBalance() ?
                    0.0 : userBalance.getBalance() - amount);
            userBalanceRepository.save(userBalance);
        } else {
            userBalance = userBalanceRepository.save(new UserBalance(userName, 0.0));
        }

        return Optional.of(userBalance);
    }

    @Override
    public Optional<UserBalance> getMyBalance(HttpServletRequest request) {
        String userName = CurrentUserUtils.getCurrentUser(request);
        UserBalance userBalance = userBalanceRepository.findUserBalanceByUserName(userName);
        return Optional.ofNullable(userBalance);
    }
}