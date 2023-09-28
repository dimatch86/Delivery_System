package ru.skillbox.inventorysrvice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.skillbox.dto.InventoryCompensationDto;
import ru.skillbox.dto.PaymentCompensationDto;
import ru.skillbox.dto.ProductDetailsDto;
import ru.skillbox.inventorysrvice.domain.Product;
import ru.skillbox.inventorysrvice.repository.InventoryRepository;
import ru.skillbox.inventorysrvice.service.InventoryKafkaService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryCompensationTask {

    private final InventoryRepository inventoryRepository;
    private final InventoryKafkaService inventoryKafkaService;

    @KafkaListener(topics = "reverse-inventory", groupId = "inventory-service", containerFactory = "kafkaListenerInventoryContainerFactory")
    public void onCompensation(InventoryCompensationDto inventoryCompensationDto) {

        List<ProductDetailsDto> productDetails = inventoryCompensationDto.getProducts();
        productDetails.forEach(productDetail -> {
            Product product = inventoryRepository.findProductByName(productDetail.getName());
            if (product != null) {
                product.setQuantity(product.getQuantity() + productDetail.getAmount());
                inventoryRepository.save(product);
            }
        });
        inventoryKafkaService.reversePayment(new PaymentCompensationDto(inventoryCompensationDto.getUserName(), inventoryCompensationDto.getPaymentAmount()));
        log.info("Run compensation task for inventory-service");
    }
}
