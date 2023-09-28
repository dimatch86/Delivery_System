package ru.skillbox.paymentservice.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.skillbox.dto.OrderKafkaDto;
import ru.skillbox.dto.PaymentCompensationDto;

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
    public ConsumerFactory<String, PaymentCompensationDto> paymentConsumerFactory(KafkaProperties kafkaProperties) {
        final JsonDeserializer<PaymentCompensationDto> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentCompensationDto>
    kafkaListenerPaymentContainerFactory(ConsumerFactory<String, PaymentCompensationDto> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, PaymentCompensationDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
