package ru.skillbox.inventorysrvice.service;

import ru.skillbox.dto.OrderKafkaDto;
import ru.skillbox.dto.PaymentCompensationDto;

public interface InventoryKafkaService {

    void produceToDelivery(OrderKafkaDto orderKafkaDto);

    void reversePayment(PaymentCompensationDto compensationDto);
}
