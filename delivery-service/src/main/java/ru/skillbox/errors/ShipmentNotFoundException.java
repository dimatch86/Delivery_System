package ru.skillbox.errors;

public class ShipmentNotFoundException extends RuntimeException {

    public ShipmentNotFoundException(Long shipmentId) {
        super(String.format("Could not find shipment %s.", shipmentId));
    }
}
