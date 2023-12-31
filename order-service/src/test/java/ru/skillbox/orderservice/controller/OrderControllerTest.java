package ru.skillbox.orderservice.controller;

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

import ru.skillbox.dto.OrderStatus;
import ru.skillbox.dto.StatusDto;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.OrderDto;

import ru.skillbox.orderservice.repository.OrderRepository;
import ru.skillbox.orderservice.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderService orderService;

    @Configuration
    @ComponentScan(basePackageClasses = {OrderController.class})
    public static class TestConf {
    }

    private Order order;

    private Order newOrder;

    private List<Order> orders;

    @BeforeEach
    public void setUp() {
        order = new Order(
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Tulskaya 24",
                "Order #112",

                OrderStatus.REGISTERED
        );
        newOrder = new Order(
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Dubininskaya 39",
                "Order #342",

                OrderStatus.REGISTERED
        );
        orders = Collections.singletonList(order);
    }

    @Test
    void listOrders() throws Exception {
        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        mvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(order.getDescription())));
    }

    @Test
    void listOrder() throws Exception {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        mvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(order.getDescription())));
    }

    @Test
    void addOrder() throws Exception {

        Mockito.when(orderService.addOrder(any(OrderDto.class), any(HttpServletRequest.class))).thenReturn(Optional.of(newOrder));
        mvc.perform(
                        post("/order")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"description\":\"Order #342\",\"departureAddress\":\"Moscow, st.Taganskaya 150\"," +
                                                "\"destinationAddress\":\"Moscow, st.Dubininskaya 39\"," +
                                                "\"productDetails\":[{\"name\":\"Milk\", \"cost\":10.0, \"amount\":2}," +
                                                "{\"name\":\"Coffee\", \"cost\":15.0, \"amount\":2}]}}"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void updateOrderStatus() throws Exception {
        StatusDto statusDto = new StatusDto(OrderStatus.PAID);
        doNothing().when(orderService).updateOrderStatus(1L, statusDto);
        mvc.perform(
                        patch("/order/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\"status\": \"PAID\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void updateOrderStatusWithError() throws Exception {
        StatusDto statusDto = new StatusDto(OrderStatus.PAID);
        doThrow(new RuntimeException()).when(orderService).updateOrderStatus(1L, statusDto);
        mvc.perform(
                        patch("/order/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\"status\": \"PAID\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotModified());
    }

}
