package ru.skillbox.service;


import ru.skillbox.domain.Shipment;

import java.util.List;

public interface ShipmentService {

    List<Shipment> getShipments();
    Shipment getShipment(Long id);
}
