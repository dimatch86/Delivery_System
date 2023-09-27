package ru.skillbox.inventorysrvice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.inventorysrvice.errors.ProductNotFoundException;
import ru.skillbox.inventorysrvice.domain.Product;
import ru.skillbox.inventorysrvice.domain.dto.ProductDto;
import ru.skillbox.inventorysrvice.domain.dto.QuantityDto;
import ru.skillbox.inventorysrvice.repository.InventoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public List<Product> getProducts() {
        return inventoryRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Product> addProduct(ProductDto productDto) {
        Product product = productDtoToProduct(productDto);
        inventoryRepository.save(product);

        return Optional.of(product);

    }

    private Product productDtoToProduct(ProductDto productDto) {
        return new Product(productDto.getName(), productDto.getQuantity());
    }

    @Override
    @Transactional
    public Optional<Product> increaseProductQuantity(String productName, QuantityDto quantityDto) {
        Product product = Optional.of(inventoryRepository.findProductByName(productName))
                .orElseThrow(ProductNotFoundException::new);

        product.setQuantity(product.getQuantity() + quantityDto.getQuantity());
        inventoryRepository.save(product);
        return Optional.of(product);
    }
}
