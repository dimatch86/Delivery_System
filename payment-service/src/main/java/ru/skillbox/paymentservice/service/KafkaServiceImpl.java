package ru.skillbox.paymentservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.OrderKafkaDto;

@Service
@Slf4j
public class KafkaServiceImpl implements KafkaService {

    @Value("${spring.kafka.topic.name}")
    private String kafkaTopicInventory;

    private final KafkaTemplate<Long, Object> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<Long, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produceToInventory(OrderKafkaDto orderKafkaDto) {
        kafkaTemplate.send(kafkaTopicInventory, orderKafkaDto);
        log.info("Sent message to inventory-service -> '{}'", orderKafkaDto);
    }
}
