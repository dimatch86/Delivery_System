package ru.skillbox.service;

import ru.skillbox.dto.InventoryCompensationDto;

public interface DeliveryKafkaService {

    void reverseInventory(InventoryCompensationDto compensationDto);
}
