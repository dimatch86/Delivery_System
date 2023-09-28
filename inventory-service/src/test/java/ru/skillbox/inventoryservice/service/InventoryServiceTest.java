package ru.skillbox.inventoryservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.skillbox.inventorysrvice.errors.ProductNotFoundException;
import ru.skillbox.inventorysrvice.domain.Product;
import ru.skillbox.inventorysrvice.domain.dto.ProductDto;
import ru.skillbox.inventorysrvice.domain.dto.QuantityDto;
import ru.skillbox.inventorysrvice.repository.InventoryRepository;
import ru.skillbox.inventorysrvice.service.InventoryServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private static final int AMOUNT = 100;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProducts() {

        Product product1 = new Product("Milk", 10);
        Product product2 = new Product("Coffee", 12);

        Mockito.when(inventoryRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> products = inventoryService.getProducts();
        assertEquals(2, products.size());

    }

    @Test
    void testAddProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Milk");
        productDto.setQuantity(10);

        when(inventoryRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Optional<Product> product = inventoryService.addProduct(productDto);

        assertTrue(product.isPresent());
        assertEquals(productDto.getName(), product.get().getName());
    }

    @Test
    void testIncreaseProductQuantity() {
        Product product = new Product("Milk", 10);
        QuantityDto quantityDto = new QuantityDto();
        quantityDto.setQuantity(AMOUNT);

        when(inventoryRepository.findProductByName(product.getName())).thenReturn(product);

        Optional<Product> result = inventoryService.increaseProductQuantity(product.getName(), quantityDto);

        assertTrue(result.isPresent());
        Product updatedProduct = result.get();
        assertEquals(product, updatedProduct);
        assertEquals(110, updatedProduct.getQuantity());
        verify(inventoryRepository, times(1)).save(product);
    }

    @Test
    void testIncreaseProductQuantityNotExisting() {

        String productName = "Coffee";
        QuantityDto quantityDto = new QuantityDto();
        quantityDto.setQuantity(AMOUNT);

        when(inventoryRepository.findProductByName(productName)).thenReturn(null);
        doThrow(new ProductNotFoundException()).when(inventoryRepository).findProductByName(productName);

        assertThrows(ProductNotFoundException.class, () -> inventoryService.increaseProductQuantity(productName, quantityDto));
        verify(inventoryRepository, never()).save(any());
    }
}
