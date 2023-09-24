package ru.skillbox.inventorysrvice.controller;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super("Product not found or not enough quantity for this product!");
    }
}
