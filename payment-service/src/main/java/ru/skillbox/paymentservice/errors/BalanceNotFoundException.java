package ru.skillbox.paymentservice.errors;

public class BalanceNotFoundException extends RuntimeException {

    public BalanceNotFoundException() {
        super("User balance not found");
    }
}
