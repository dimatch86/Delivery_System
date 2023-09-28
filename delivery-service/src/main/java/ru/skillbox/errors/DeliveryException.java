package ru.skillbox.errors;

public class DeliveryException extends RuntimeException {
    public DeliveryException() {
        super("Delivery failed");
    }
}
