package ru.skillbox.paymentservice.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.dto.OrderKafkaDto;
import ru.skillbox.dto.OrderStatus;
import ru.skillbox.dto.ServiceName;
import ru.skillbox.dto.StatusDto;
import ru.skillbox.paymentservice.controller.PaymentException;
import ru.skillbox.paymentservice.domain.Payment;
import ru.skillbox.paymentservice.domain.PaymentStatus;
import ru.skillbox.paymentservice.domain.UserBalance;
import ru.skillbox.paymentservice.repository.PaymentRepository;
import ru.skillbox.paymentservice.repository.UserBalanceRepository;
import ru.skillbox.paymentservice.service.KafkaService;
import ru.skillbox.util.RestUtil;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumer {

    @Value("${payment.duration}")
    private int paymentDuration;

    private final UserBalanceRepository userBalanceRepository;
    private final PaymentRepository paymentRepository;
    private final KafkaService kafkaService;
    private final RestTemplate restTemplate;

    @KafkaListener(topics = "order", groupId = "payment-service", containerFactory = "kafkaListenerOrderContainerFactory")
    public void onMessage(OrderKafkaDto orderKafkaDto) {
        Payment payment = orderDtoToPayment(orderKafkaDto);
        OrderStatus orderStatus = null;
        try {

            checkAndUpdateUserBalance(orderKafkaDto.getUserName(), orderKafkaDto.getOrderPrice());

            orderStatus = OrderStatus.PAID;
            kafkaService.produceToInventory(orderKafkaDto);

        } catch (PaymentException e) {
            payment.setStatus(PaymentStatus.DECLINED);
            orderStatus = OrderStatus.PAYMENT_FAILED;
            log.warn("Process finished with error {}" , e.getMessage());

        } catch (InterruptedException e) {
            orderStatus = OrderStatus.UNEXPECTED_FAILURE;
            log.warn("Process finished with error {}" , e.getMessage());
            Thread.currentThread().interrupt();

        } finally {
            paymentRepository.save(payment);
            String message = String.format("Payment of order № %s created with status %s", orderKafkaDto.getOrderId(), payment.getStatus());

            RestUtil.createRequestForOrderStatusUpdating(restTemplate,
                    new StatusDto(orderStatus, ServiceName.PAYMENT_SERVICE, message),
                    orderKafkaDto.getOrderId(), orderKafkaDto.getUserToken());
        }
    }

    private Payment orderDtoToPayment(OrderKafkaDto orderKafkaDto) {
        return new Payment(orderKafkaDto.getOrderPrice(),
                orderKafkaDto.getOrderId(),
                LocalDateTime.now(),
                PaymentStatus.APPROVED);
    }

    private void checkAndUpdateUserBalance(String userName, double orderPrice) throws InterruptedException {
        Thread.sleep(paymentDuration);
        UserBalance userBalance = userBalanceRepository.findUserBalanceByUserName(userName);

        if (userBalance == null || userBalance.getBalance() < orderPrice) {
            throw new PaymentException();
        }
        userBalance.setBalance(userBalance.getBalance() - orderPrice);
        userBalanceRepository.save(userBalance);

    }
}
