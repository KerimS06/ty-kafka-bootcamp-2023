package com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry;

import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.producer.RetryProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedMessageRetryProducer
        extends RetryProducer<OrderCreatedMessage> {

    @Value("${spring.kafka.consumer.order.created.retry.topic}")
    private String retryTopic;

    @Value("${spring.kafka.consumer.order.created.retry.retry-count}")
    private Integer retryCount;

    @Value("${spring.kafka.consumer.order.created.retry.retry-interval}")
    private Long retryInterval;

    @Value("${spring.kafka.consumer.order.created.error.topic}")
    private String errorTopic;

    public OrderCreatedMessageRetryProducer(KafkaTemplate<String, ?> kafkaTemplate) {
        super(kafkaTemplate);
    }

    @Override
    protected String getRetryTopic() {
        return retryTopic;
    }

    @Override
    protected String getErrorTopic() {
        return errorTopic;
    }

    @Override
    public RetryTemplate getRetryTemplate() {
        return RetryTemplateFactory.getSimpleFixedRetryTemplate(retryInterval, retryCount);
    }

    @Override
    protected String partitionKey(OrderCreatedMessage message) {
        return String.valueOf(message.getOrderId());
    }
}
