package ru.skillbox.paymentservice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.skillbox.dto.PaymentCompensationDto;
import ru.skillbox.paymentservice.domain.UserBalance;
import ru.skillbox.paymentservice.repository.UserBalanceRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCompensationTask {

    private final UserBalanceRepository userBalanceRepository;

    @KafkaListener(topics = "reverse-payment", groupId = "inventory-service", containerFactory = "kafkaListenerPaymentContainerFactory")
    public void onCompensation(PaymentCompensationDto compensationDto) {

        UserBalance userBalance = userBalanceRepository.findUserBalanceByUserName(compensationDto.getUserName());
        if (userBalance != null) {
            userBalance.setBalance(userBalance.getBalance() + compensationDto.getPaymentAmount());
            userBalanceRepository.save(userBalance);
        }
        log.info("Run compensation task for payment-service");
    }
}