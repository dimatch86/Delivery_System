package ru.skillbox.paymentservice.controller;

public class PaymentException extends RuntimeException {

    public PaymentException() {
        super("User balance not found or less than order price");
    }
}
