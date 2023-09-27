package ru.skillbox.paymentservice.errors;

public class PaymentException extends RuntimeException {

    public PaymentException() {
        super("User balance not found or less than order price");
    }
}
