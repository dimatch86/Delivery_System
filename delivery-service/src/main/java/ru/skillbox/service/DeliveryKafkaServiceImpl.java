package ru.skillbox.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.InventoryCompensationDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryKafkaServiceImpl implements DeliveryKafkaService {

    @Value("${spring.kafka.topic.name}")
    private String kafkaTopic;

    private final KafkaTemplate<Long, Object> kafkaTemplate;

    @Override
    public void reverseInventory(InventoryCompensationDto compensationDto) {
        kafkaTemplate.send(kafkaTopic, compensationDto);
        log.info("Run compensation task for inventory-service -> '{}'", compensationDto);
    }
}
