package com.trendyol.kafkabootcamp2023.orderservice.messaging.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;

import java.util.Objects;

public abstract class AbstractKafkaProducer<T> {

    protected final KafkaTemplate<String, ?> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(AbstractKafkaProducer.class);

    protected AbstractKafkaProducer(KafkaTemplate<String, ?> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produce(T message, String producerTopic) {
        if (Objects.isNull(message)) {
            throw new IllegalArgumentException("Kafka producer message can not be null!");
        }

        if (!StringUtils.hasText(producerTopic)) {
            throw new IllegalArgumentException("Kafka producer topic can not be null or empty!");
        }

        String key = partitionKey(message);
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Kafka producer partition key can not be null or empty!");
        }

        Message<T> producerMessage = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, producerTopic)
                .setHeader(KafkaHeaders.MESSAGE_KEY, key)
                .build();

        kafkaTemplate.send(producerMessage);
        logger.info("Message sent, message: {}, topic: {}, key: {}", message, producerTopic, key);
    }

    protected abstract String partitionKey(T message);
}
