package com.trendyol.kafkabootcamp2023.orderservice.messaging.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.StringUtils;

import java.util.Objects;

public abstract class RetryProducer<T>
        extends AbstractKafkaProducer<T> {

    private final Logger logger = LoggerFactory.getLogger(RetryProducer.class);


    protected RetryProducer(KafkaTemplate<String, ?> kafkaTemplate) {
        super(kafkaTemplate);
    }

    protected abstract String getRetryTopic();

    protected abstract String getErrorTopic();

    public abstract RetryTemplate getRetryTemplate();

    public void sendRetryMessage(T message) {

        if (Objects.isNull(message)) {
            throw new IllegalArgumentException("Kafka retry producer retry message can not be null!");
        }

        if (!StringUtils.hasText(getRetryTopic())) {
            throw new IllegalArgumentException("Kafka retry producer retry topic can not be null or empty!");
        }

        String key = partitionKey(message);
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Kafka retry producer retry partition key can not be null or empty!");
        }

        Message<T> retryMessage = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, getRetryTopic())
                .setHeader(KafkaHeaders.MESSAGE_KEY, key)
                .build();
        kafkaTemplate.send(retryMessage);
        logger.info("{} retry message is sent: {}, topic: {}, key: {}", getClass().getSimpleName(), message, getRetryTopic(), key);
    }

    public void sendErrorMessage(T message) {

        if (Objects.isNull(message)) {
            throw new IllegalArgumentException("Kafka retry producer error message can not be null!");
        }

        if (!StringUtils.hasText(getErrorTopic())) {
            throw new IllegalArgumentException("Kafka retry producer error topic can not be null or empty!");
        }

        String key = partitionKey(message);
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Kafka retry producer error partition key can not be null or empty!");
        }

        Message<T> errorMessage = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, getErrorTopic())
                .setHeader(KafkaHeaders.MESSAGE_KEY, key)
                .build();
        kafkaTemplate.send(errorMessage);
        logger.info("{} error message is sent: {}, topic: {}, key: {}", getClass().getSimpleName(), message, getErrorTopic(), key);
    }

}
