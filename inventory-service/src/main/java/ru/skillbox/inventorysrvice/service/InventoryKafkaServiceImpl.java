package ru.skillbox.inventorysrvice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.OrderKafkaDto;
import ru.skillbox.dto.PaymentCompensationDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryKafkaServiceImpl implements InventoryKafkaService {

    @Value("${spring.kafka.topic.name}")
    private String kafkaTopic;

    @Value("${spring.kafka.compensation.topic.name}")
    private String compensationKafkaTopic;

    private final KafkaTemplate<Long, Object> kafkaTemplate;

    @Override
    public void produceToDelivery(OrderKafkaDto orderKafkaDto) {
        kafkaTemplate.send(kafkaTopic, orderKafkaDto);
        log.info("Sent message to Delivery-service -> '{}'", orderKafkaDto);
    }

    @Override
    public void reversePayment(PaymentCompensationDto compensationDto) {
        kafkaTemplate.send(compensationKafkaTopic, compensationDto);
        log.info("Run compensation task for payment-service -> '{}'", compensationDto);
    }
}
