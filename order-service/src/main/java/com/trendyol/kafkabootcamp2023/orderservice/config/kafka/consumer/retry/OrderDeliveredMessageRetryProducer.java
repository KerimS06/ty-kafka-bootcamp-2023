package com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry;

import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderDeliveredMessage;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.producer.RetryProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderDeliveredMessageRetryProducer
        extends RetryProducer<OrderDeliveredMessage> {

    @Value("${spring.kafka.consumer.order.delivered.retry.topic}")
    private String retryTopic;

    @Value("${spring.kafka.consumer.order.delivered.retry.retry-count}")
    private Integer retryCount;

    @Value("${spring.kafka.consumer.order.delivered.retry.retry-interval}")
    private Long retryInterval;

    @Value("${spring.kafka.consumer.order.delivered.error.topic}")
    private String errorTopic;

    public OrderDeliveredMessageRetryProducer(KafkaTemplate<String, ?> kafkaTemplate) {
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
    protected String partitionKey(OrderDeliveredMessage message) {
        return String.valueOf(message.getOrderId());
    }
}
