package ru.skillbox.inventorysrvice.configuration;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.skillbox.dto.InventoryCompensationDto;
import ru.skillbox.dto.OrderKafkaDto;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {


    @Bean
    public ConsumerFactory<String, OrderKafkaDto> orderConsumerFactory(KafkaProperties kafkaProperties) {
        final JsonDeserializer<OrderKafkaDto> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderKafkaDto>
    kafkaListenerOrderContainerFactory(ConsumerFactory<String, OrderKafkaDto> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, OrderKafkaDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, InventoryCompensationDto> inventoryConsumerFactory(KafkaProperties kafkaProperties) {
        final JsonDeserializer<InventoryCompensationDto> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InventoryCompensationDto>
    kafkaListenerInventoryContainerFactory(ConsumerFactory<String, InventoryCompensationDto> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, InventoryCompensationDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
