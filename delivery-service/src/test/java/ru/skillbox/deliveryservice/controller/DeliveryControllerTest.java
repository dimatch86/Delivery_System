package ru.skillbox.deliveryservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.controller.DeliveryController;
import ru.skillbox.domain.Shipment;
import ru.skillbox.service.ShipmentServiceImpl;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ShipmentServiceImpl shipmentService;

    @Configuration
    @ComponentScan(basePackageClasses = {DeliveryController.class})
    public static class TestConf {
    }

    private Shipment shipment1;
    private Shipment shipment2;

    @BeforeEach
    public void setUp() {
        shipment1 = new Shipment("Moscow, st.Taganskaya 150", "Moscow, st.Tulskaya 24");
        shipment2 = new Shipment("Moscow, st.Taganskaya 150", "Moscow, st.Dubininskaya 39");
    }

    @Test
    void testGetShipments() throws Exception {
        Mockito.when(shipmentService.getShipments()).thenReturn(List.of(shipment1, shipment2));
        mvc.perform(get("/shipment"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(shipment1.getDepartureAddress())))
                .andExpect(content().string(containsString(shipment2.getDestinationAddress())));
    }

    @Test
    void listShipment() throws Exception {
        Mockito.when(shipmentService.getShipment(1L)).thenReturn(shipment1);
        mvc.perform(get("/shipment/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(shipment1.getDestinationAddress())));
    }
}
