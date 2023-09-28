package ru.skillbox.deliveryservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.skillbox.domain.Shipment;
import ru.skillbox.errors.ShipmentNotFoundException;
import ru.skillbox.repository.ShipmentRepository;
import ru.skillbox.service.ShipmentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetShipments() {

        Shipment shipment1 = new Shipment("Moscow, st.Taganskaya 150", "Moscow, st.Tulskaya 24");
        Shipment shipment2 = new Shipment("Moscow, st.Taganskaya 150", "Moscow, st.Dubininskaya 39");

        Mockito.when(shipmentRepository.findAll()).thenReturn(List.of(shipment1, shipment2));

        List<Shipment> shipments = shipmentService.getShipments();
        assertEquals(2, shipments.size());

    }

    @Test
    void testGetShipment() {

        Shipment shipment1 = new Shipment("Moscow, st.Taganskaya 150", "Moscow, st.Tulskaya 24");

        Mockito.when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment1));
        Shipment shipment = shipmentService.getShipment(1L);
        assertNotNull(shipment);
        assertEquals(shipment.getDepartureAddress(), shipment1.getDepartureAddress());

    }

    @Test
    void testGetShipmentNotExisted() {

        doThrow(new ShipmentNotFoundException(1L)).when(shipmentRepository).findById(1L);

        assertThrows(ShipmentNotFoundException.class, () -> shipmentRepository.findById(1L));
        verify(shipmentRepository, never()).save(any());

    }
}
