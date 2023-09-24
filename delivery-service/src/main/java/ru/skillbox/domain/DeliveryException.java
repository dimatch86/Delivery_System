package ru.skillbox.domain;

public class DeliveryException extends RuntimeException {
    public DeliveryException() {
        super("Delivery failed");
    }
}
