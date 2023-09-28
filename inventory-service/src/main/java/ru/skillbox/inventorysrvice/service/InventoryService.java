package ru.skillbox.inventorysrvice.service;

import ru.skillbox.inventorysrvice.domain.Product;
import ru.skillbox.inventorysrvice.domain.dto.ProductDto;
import ru.skillbox.inventorysrvice.domain.dto.QuantityDto;

import java.util.List;
import java.util.Optional;

public interface InventoryService {

    List<Product> getProducts();

    Optional<Product> addProduct(ProductDto productDto);

    Optional<Product> increaseProductQuantity(String productName, QuantityDto quantityDto);
}
