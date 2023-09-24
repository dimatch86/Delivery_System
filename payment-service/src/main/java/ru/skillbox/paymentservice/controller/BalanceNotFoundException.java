package ru.skillbox.paymentservice.controller;

public class BalanceNotFoundException extends RuntimeException {

    public BalanceNotFoundException() {
        super("User balance not found");
    }
}
