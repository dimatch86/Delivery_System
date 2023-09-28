package ru.skillbox.inventorysrvice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.inventorysrvice.domain.Product;
import ru.skillbox.inventorysrvice.domain.dto.ProductDto;
import ru.skillbox.inventorysrvice.domain.dto.QuantityDto;
import ru.skillbox.inventorysrvice.service.InventoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @Operation(summary = "List all products in delivery system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/product")
    public List<Product> listProducts() {
        return inventoryService.getProducts();
    }

    @Operation(summary = "Add product to stock", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody @Valid ProductDto productDto) {

        return inventoryService.addProduct(productDto)
                .map(product -> ResponseEntity.status(HttpStatus.OK).body(product))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build());
    }

    @Operation(summary = "Increase quantity of product", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/add/{productName}")
    public ResponseEntity<Product> increaseProductQuantity(@PathVariable String productName, @RequestBody @Valid QuantityDto quantityDto) {

        return inventoryService.increaseProductQuantity(productName, quantityDto)
                .map(product -> ResponseEntity.status(HttpStatus.OK).body(product))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
    }
}
