package com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.order;

import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.AbstractKafkaConsumerConfigurer;
import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.KafkaConsumerFactorySupport;
import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.KafkaConsumerProperties;
import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.GlobalKafkaRetryListenerSupport;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.OrderCreatedMessageRetryProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;


@Configuration
@EnableConfigurationProperties(OrderCreatedMessageConsumerConfig.OrderCreatedMessageConsumerProperties.class)
public class OrderCreatedMessageConsumerConfig
        extends AbstractKafkaConsumerConfigurer<OrderCreatedMessage> {

    public OrderCreatedMessageConsumerConfig(KafkaConsumerFactorySupport kafkaConsumerFactorySupport,
                                             OrderCreatedMessageConsumerProperties propertySource,
                                             GlobalKafkaRetryListenerSupport retryListenerSupport,
                                             OrderCreatedMessageRetryProducer retryProducer) {
        super(kafkaConsumerFactorySupport, propertySource, retryListenerSupport, retryProducer);
    }

    @Bean
    @ConditionalOnProperty(value = "spring.kafka.consumer.order.created.enabled", havingValue = "true")
    ConcurrentKafkaListenerContainerFactory<String, OrderCreatedMessage> orderCreatedKafkaListenerContainerFactory() {
        return createConsumerListenerFactory();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.kafka.consumer.order.created.retry.enabled", havingValue = "true")
    ConcurrentKafkaListenerContainerFactory<String, OrderCreatedMessage> orderCreatedRetryKafkaListenerContainerFactory() {
        return createRetryListenerFactory();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.kafka.consumer.order.created.error.enabled", havingValue = "true")
    ConcurrentKafkaListenerContainerFactory<String, OrderCreatedMessage> orderCreatedErrorKafkaListenerContainerFactory() {
        return createErrorListenerFactory();
    }

    @ConfigurationProperties("spring.kafka.consumer.order.created")
    public static class OrderCreatedMessageConsumerProperties
            extends KafkaConsumerProperties {
    }


}