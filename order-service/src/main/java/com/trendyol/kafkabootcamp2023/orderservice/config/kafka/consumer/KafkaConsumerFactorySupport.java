package com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaConsumerFactorySupport {

    private final ObjectMapper objectMapper;
    private final ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> consumerFactoryCustomizers;
    private final KafkaErrorHandler kafkaErrorHandler;

    public KafkaConsumerFactorySupport(ObjectMapper objectMapper,
                                       ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> consumerFactoryCustomizers,
                                       KafkaErrorHandler kafkaErrorHandler) {
        this.objectMapper = objectMapper;
        this.consumerFactoryCustomizers = consumerFactoryCustomizers;
        this.kafkaErrorHandler = kafkaErrorHandler;
    }

    public <V> ConcurrentKafkaListenerContainerFactory<String, V> createListenerContainerFactory(Class<V> valueClass,
                                                                                                 Map<String, Object> configs,
                                                                                                 boolean autoStartup) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, V>();
        factory.setConsumerFactory(kafkaConsumerFactory(configs, valueClass));
        factory.setAutoStartup(autoStartup);
        factory.setErrorHandler(kafkaErrorHandler);
        return factory;
    }

    private <V> ConsumerFactory<String, V> kafkaConsumerFactory(Map<String, Object> configParams, Class<V> valueClass) {
        var typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);

        var keyDeserializer = new StringDeserializer();
        var jsonDeserializer = new JsonDeserializer<>(valueClass, objectMapper);
        jsonDeserializer.setTypeMapper(typeMapper);


        var consumerFactory = new DefaultKafkaConsumerFactory<>(
                configParams,
                new ErrorHandlingDeserializer<>(keyDeserializer),
                new ErrorHandlingDeserializer<>(jsonDeserializer)
        );

        consumerFactoryCustomizers
                .stream()
                .forEach(customizer -> customizer.customize(consumerFactory));

        return consumerFactory;
    }
}
