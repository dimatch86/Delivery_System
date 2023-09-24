package ru.skillbox.orderservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.dto.OrderStatus;
import ru.skillbox.dto.ServiceName;
import ru.skillbox.dto.StatusDto;
import ru.skillbox.util.CurrentUserUtils;
import ru.skillbox.dto.OrderKafkaDto;
import ru.skillbox.orderservice.controller.OrderNotFoundException;
import ru.skillbox.orderservice.domain.*;
import ru.skillbox.orderservice.repository.OrderRepository;
import ru.skillbox.orderservice.util.KafkaOrderDtoMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final KafkaService kafkaService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, KafkaService kafkaService) {
        this.orderRepository = orderRepository;
        this.kafkaService = kafkaService;
    }

    @Transactional
    @Override
    public Optional<Order> addOrder(OrderDto orderDto, HttpServletRequest request) {

        String user = CurrentUserUtils.getCurrentUser(request);

        Order newOrder = new Order(
                orderDto.getDescription(),
                orderDto.getDepartureAddress(),
                orderDto.getDestinationAddress(),
                calculateOrderCost(orderDto),
                LocalDateTime.now(),
                LocalDateTime.now(),
                OrderStatus.REGISTERED,
                user
        );
        newOrder.addStatusHistory(newOrder.getStatus(), ServiceName.ORDER_SERVICE, "Order created");
        newOrder.addProductDetails(orderDto.getProductDetailsDto());
        Order order = orderRepository.save(newOrder);
        OrderKafkaDto orderKafkaDto = KafkaOrderDtoMapper.toOrderKafkaDto(order);
        orderKafkaDto.setUserToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        kafkaService.produce(orderKafkaDto);
        return Optional.of(order);
    }

    @Transactional
    @Override
    public void updateOrderStatus(Long id, StatusDto statusDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        if (order.getStatus() == statusDto.getStatus()) {
            log.info("Request with same status {} for order {} from service {}", statusDto.getStatus(), id, statusDto.getServiceName());
            return;
        }
        order.setModifiedTime(LocalDateTime.now());
        order.setStatus(statusDto.getStatus());
        order.addStatusHistory(statusDto.getStatus(), statusDto.getServiceName(), statusDto.getComment());
        orderRepository.save(order);
    }

    private double calculateOrderCost(OrderDto orderDto) {
        return orderDto.getProductDetailsDto()
                .stream()
                .map(productDetailsDto -> productDetailsDto.getCost() * productDetailsDto.getAmount())
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
