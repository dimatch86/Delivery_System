package ru.skillbox.paymentservice.service;


import ru.skillbox.dto.OrderKafkaDto;

public interface KafkaService {

    void produceToInventory(OrderKafkaDto orderKafkaDto);
}
