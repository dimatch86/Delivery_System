package ru.skillbox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.domain.Shipment;
import ru.skillbox.errors.ShipmentNotFoundException;
import ru.skillbox.repository.ShipmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    @Override
    public List<Shipment> getShipments() {
        return shipmentRepository.findAll();
    }

    @Override
    public Shipment getShipment(Long id) {
        return shipmentRepository.findById(id).orElseThrow(() -> new ShipmentNotFoundException(id));
    }
}
