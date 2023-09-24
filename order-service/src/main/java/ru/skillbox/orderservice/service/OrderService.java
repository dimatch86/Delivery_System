package ru.skillbox.orderservice.service;


import ru.skillbox.dto.StatusDto;
import ru.skillbox.orderservice.domain.Order;
import ru.skillbox.orderservice.domain.OrderDto;


import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface OrderService {

    Optional<Order> addOrder(OrderDto orderDto, HttpServletRequest request);

    void updateOrderStatus(Long id, StatusDto statusDto);
}
