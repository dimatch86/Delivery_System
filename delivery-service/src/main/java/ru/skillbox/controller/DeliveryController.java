package ru.skillbox.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.domain.Shipment;
import ru.skillbox.service.ShipmentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DeliveryController {
    private final ShipmentService shipmentService;

    @Operation(summary = "List all shipments in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/shipment")
    public List<Shipment> listShipments() {
        return shipmentService.getShipments();
    }

    @Operation(summary = "Get shipment in delivery system by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/shipment/{shipmentId}")
    public Shipment getShipment(@PathVariable @Parameter(description = "Id of shipment") Long shipmentId) {
        return shipmentService.getShipment(shipmentId);
    }
}
