package ru.skillbox.inventorysrvice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.dto.*;
import ru.skillbox.inventorysrvice.controller.ProductNotFoundException;
import ru.skillbox.inventorysrvice.domain.Product;
import ru.skillbox.inventorysrvice.repository.InventoryRepository;
import ru.skillbox.inventorysrvice.service.InventoryKafkaService;
import ru.skillbox.util.RestUtil;

import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryConsumer {

    @Value("${inventory.duration}")
    private int inventoryDuration;
    private final InventoryRepository inventoryRepository;
    private final InventoryKafkaService inventoryKafkaService;
    private final RestTemplate restTemplate;

    @KafkaListener(topics = "inventory", groupId = "inventory-service", containerFactory = "kafkaListenerOrderContainerFactory")
    public void onMessage(OrderKafkaDto orderKafkaDto) {

        OrderStatus orderStatus = null;
        HashMap<Product, Integer> products = new HashMap<>();
        try {
            checkInventoryStock(orderKafkaDto.getProductDetails(), products);
            orderStatus = OrderStatus.INVENTED;
            inventoryKafkaService.produceToDelivery(orderKafkaDto);

        } catch (ProductNotFoundException e) {
            products.clear();
            orderStatus = OrderStatus.INVENTMENT_FAILED;
            inventoryKafkaService.reversePayment(new PaymentCompensationDto(orderKafkaDto.getUserName(), orderKafkaDto.getOrderPrice()));
            log.warn("Process finished with error {}" , e.getMessage());

        } catch (InterruptedException e) {
            orderStatus = OrderStatus.UNEXPECTED_FAILURE;
            log.warn("Process finished with error {}" , e.getMessage());
            Thread.currentThread().interrupt();

        } finally {
            String message = String.format("Inventment of order № %s finished with status %s", orderKafkaDto.getOrderId(), orderStatus);

            RestUtil.createRequestForOrderStatusUpdating(restTemplate,
                    new StatusDto(orderStatus, ServiceName.INVENTORY_SERVICE, message),
                    orderKafkaDto.getOrderId(), orderKafkaDto.getUserToken());
        }

    }

    private void checkInventoryStock(List<ProductDetailsDto> productDetailsDtoList, HashMap<Product, Integer> products) throws InterruptedException {
        Thread.sleep(inventoryDuration);
        productDetailsDtoList.forEach(productDetails -> {
            Product product = inventoryRepository.findProductByName(productDetails.getName());

            if (product == null || product.getQuantity() < productDetails.getAmount()) {
                throw new ProductNotFoundException();
            }

            products.put(product, productDetails.getAmount());
        });
        products.forEach((product, quantity) ->
                product.setQuantity(product.getQuantity() - quantity));
        inventoryRepository.saveAll(products.keySet());
    }
}
