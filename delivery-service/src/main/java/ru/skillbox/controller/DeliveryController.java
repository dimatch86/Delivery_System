package ru.skillbox.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.domain.Shipment;
import ru.skillbox.repository.ShipmentRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DeliveryController {

    private final ShipmentRepository shipmentRepository;

    @Operation(summary = "List all shipments in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/shipment")
    public List<Shipment> listShipments() {
        return shipmentRepository.findAll();
    }
}
