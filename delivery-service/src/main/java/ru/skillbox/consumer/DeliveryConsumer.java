package ru.skillbox.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.errors.DeliveryException;
import ru.skillbox.domain.Shipment;
import ru.skillbox.domain.ShipmentStatus;
import ru.skillbox.dto.*;

import ru.skillbox.repository.ShipmentRepository;
import ru.skillbox.service.DeliveryKafkaService;
import ru.skillbox.util.RestUtil;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryConsumer {

    @Value("${delivery.duration}")
    private int deliveryDuration;
    @Value("${delivery.percent_of_failure}")
    private double percentOfFailure;
    private final RestTemplate restTemplate;
    private final ShipmentRepository shipmentRepository;
    private final DeliveryKafkaService deliveryKafkaService;

    @KafkaListener(topics = "delivery", groupId = "delivery-service", containerFactory = "kafkaListenerOrderContainerFactory")
    public void onMessage(OrderKafkaDto orderKafkaDto) {
        OrderStatus orderStatus = null;
        Shipment shipment = orderDtoToShipment(orderKafkaDto);

        try {
            Thread.sleep(deliveryDuration);
            if (!isDeliverySuccess()) {
                throw new DeliveryException();
            }
            orderStatus = OrderStatus.DELIVERED;

        } catch (DeliveryException e) {
            shipment.setStatus(ShipmentStatus.FAILED);
            orderStatus = OrderStatus.DELIVERY_FAILED;
            deliveryKafkaService.reverseInventory(new InventoryCompensationDto(orderKafkaDto.getUserName(), orderKafkaDto.getOrderPrice(), orderKafkaDto.getProductDetails()));
            log.warn("Process finished with error {}" , e.getMessage());

        } catch (InterruptedException e) {
            orderStatus = OrderStatus.UNEXPECTED_FAILURE;
            log.warn("Process finished with error {}" , e.getMessage());
            deliveryKafkaService.reverseInventory(new InventoryCompensationDto(orderKafkaDto.getUserName(), orderKafkaDto.getOrderPrice(), orderKafkaDto.getProductDetails()));
            Thread.currentThread().interrupt();

        } finally {
            shipmentRepository.save(shipment);
            String message = String.format("Shipment of order № %s finished with status %s", orderKafkaDto.getOrderId(), orderStatus);
            RestUtil.createRequestForOrderStatusUpdating(restTemplate,
                    new StatusDto(orderStatus, ServiceName.DELIVERY_SERVICE, message),
                    orderKafkaDto.getOrderId(), orderKafkaDto.getUserToken());
        }
    }

    private boolean isDeliverySuccess() {
        return Math.random() > percentOfFailure;
    }

    private Shipment orderDtoToShipment(OrderKafkaDto orderKafkaDto) {
        return new Shipment(orderKafkaDto.getDepartureAddress(),
                orderKafkaDto.getDestinationAddress(),
                LocalDateTime.now(),
                orderKafkaDto.getOrderId(),
                ShipmentStatus.DELIVERED);
    }
}
