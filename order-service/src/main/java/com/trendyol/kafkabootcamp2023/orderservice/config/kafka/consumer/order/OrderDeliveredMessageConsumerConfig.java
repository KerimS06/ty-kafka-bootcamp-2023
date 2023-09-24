package com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.order;

import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.AbstractKafkaConsumerConfigurer;
import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.KafkaConsumerFactorySupport;
import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.KafkaConsumerProperties;
import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.GlobalKafkaRetryListenerSupport;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderDeliveredMessage;
import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.OrderDeliveredMessageRetryProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;


@Configuration
@EnableConfigurationProperties(OrderDeliveredMessageConsumerConfig.OrderDeliveredMessageConsumerProperties.class)
public class OrderDeliveredMessageConsumerConfig
        extends AbstractKafkaConsumerConfigurer<OrderDeliveredMessage> {

    public OrderDeliveredMessageConsumerConfig(KafkaConsumerFactorySupport kafkaConsumerFactorySupport,
                                               OrderDeliveredMessageConsumerProperties propertySource,
                                               GlobalKafkaRetryListenerSupport retryListenerSupport,
                                               OrderDeliveredMessageRetryProducer retryProducer) {
        super(kafkaConsumerFactorySupport, propertySource, retryListenerSupport, retryProducer);
    }

    @Bean
    @ConditionalOnProperty(value = "spring.kafka.consumer.order.delivered.enabled", havingValue = "true")
    ConcurrentKafkaListenerContainerFactory<String, OrderDeliveredMessage> orderDeliveredKafkaListenerContainerFactory() {
        return createConsumerListenerFactory();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.kafka.consumer.order.delivered.retry.enabled", havingValue = "true")
    ConcurrentKafkaListenerContainerFactory<String, OrderDeliveredMessage> orderDeliveredRetryKafkaListenerContainerFactory() {
        return createRetryListenerFactory();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.kafka.consumer.order.delivered.error.enabled", havingValue = "true")
    ConcurrentKafkaListenerContainerFactory<String, OrderDeliveredMessage> orderDeliveredErrorKafkaListenerContainerFactory() {
        return createErrorListenerFactory();
    }

    @ConfigurationProperties("spring.kafka.consumer.order.delivered")
    public static class OrderDeliveredMessageConsumerProperties
            extends KafkaConsumerProperties {
    }


}