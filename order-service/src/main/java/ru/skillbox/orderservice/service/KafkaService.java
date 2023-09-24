package ru.skillbox.orderservice.service;

import ru.skillbox.dto.OrderKafkaDto;

public interface KafkaService {

    void produce(OrderKafkaDto orderKafkaDto);
}
