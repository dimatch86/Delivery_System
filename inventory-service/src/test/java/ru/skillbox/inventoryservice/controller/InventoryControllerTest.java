package ru.skillbox.inventoryservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.inventorysrvice.controller.InventoryController;
import ru.skillbox.inventorysrvice.errors.ProductNotFoundException;
import ru.skillbox.inventorysrvice.domain.Product;
import ru.skillbox.inventorysrvice.domain.dto.ProductDto;
import ru.skillbox.inventorysrvice.domain.dto.QuantityDto;
import ru.skillbox.inventorysrvice.service.InventoryService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private InventoryService inventoryService;

    @Configuration
    @ComponentScan(basePackageClasses = {InventoryController.class})
    public static class TestConf {
    }

    private Product product1;
    private Product product2;

    @BeforeEach
    public void setUp() {
        product1 = new Product("Milk", 10);
        product2 = new Product("Coffee", 12);
    }

    @Test
    void testGetProducts() throws Exception {
        Mockito.when(inventoryService.getProducts()).thenReturn(List.of(product1, product2));
        mvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(product1.getName())))
                .andExpect(content().string(containsString(product2.getName())));
    }

    @Test
    void testAddProduct() throws Exception {
        Mockito.when(inventoryService.addProduct(any(ProductDto.class))).thenReturn(Optional.of(product1));
        mvc.perform(
                        post("/add")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"name\":\"milk\",\"quantity\":10}"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(10)))
                .andExpect(jsonPath("$.name", is("Milk")));
    }

    @Test
    void testIncreaseProductQuantity() throws Exception {
        String name = "Milk";
        QuantityDto quantityDto = new QuantityDto();
        quantityDto.setQuantity(10);
        product1.setQuantity(product1.getQuantity() + quantityDto.getQuantity());
        Mockito.when(inventoryService.increaseProductQuantity(name, quantityDto)).thenReturn(Optional.of(product1));
        mvc.perform(
                        patch("/add/Milk")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"quantity\":10}"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(20)))
                .andExpect(jsonPath("$.name", is("Milk")));
    }

    @Test
    void testIncreaseProductQuantityWithError() throws Exception {
        String name = "Milk";
        QuantityDto quantityDto = new QuantityDto();
        quantityDto.setQuantity(10);
        doThrow(new ProductNotFoundException()).when(inventoryService).increaseProductQuantity(name, quantityDto);
        mvc.perform(
                        patch("/add/Milk")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"quantity\":10}"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest() );
    }
}
