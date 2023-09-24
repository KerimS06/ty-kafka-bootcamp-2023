package com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ContainerAwareErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class KafkaErrorHandler
        implements ContainerAwareErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaErrorHandler.class);


    @Override
    public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer, MessageListenerContainer container) {
        doSeeks(records, consumer);

        if (!records.isEmpty()) {
            ConsumerRecord<?, ?> consumerRecord = records.get(0);

            if (thrownException instanceof ListenerExecutionFailedException) {
                if (Objects.nonNull(thrownException.getCause()) && thrownException.getCause() instanceof DeserializationException) {
                    ExceptionUtils.getThrowableList(thrownException).stream().filter(DeserializationException.class::isInstance)
                            .findFirst().ifPresent(exception -> {
                        var deserializationException = (DeserializationException) thrownException.getCause();
                        var malformedMessage = new String(deserializationException.getData(), StandardCharsets.UTF_8);
                        LOG.error("DeserializationException occurred during consuming message with consumerRecord: {} " +
                                        "- malformed message: {}, exception: {}",
                                consumerRecord, malformedMessage, deserializationException.getLocalizedMessage());
                    });
                } else {
                    LOG.error("ListenerExecutionFailedException occurred during consuming message with consumerRecord: {} " +
                                    "- exception {}",
                            consumerRecord, thrownException.getLocalizedMessage());
                }
            } else {
                LOG.error("Exception occurred during consuming message with consumerRecord: {} " +
                                "- exception {}",
                        consumerRecord, thrownException.getLocalizedMessage());
            }
        } else {
            LOG.error("Consumer exception - cause: {}", thrownException.getLocalizedMessage());
        }
    }

    private void doSeeks(List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer) {
        Map<TopicPartition, Long> partitions = new LinkedHashMap<>();
        var first = new AtomicBoolean(true);
        records.forEach(consumerRecord -> {
            if (first.get()) {
                partitions.put(new TopicPartition(consumerRecord.topic(), consumerRecord.partition()), consumerRecord.offset() + 1);
            } else {
                partitions.computeIfAbsent(new TopicPartition(consumerRecord.topic(), consumerRecord.partition()),
                        offset -> consumerRecord.offset());
            }
            first.set(false);
        });
        partitions.forEach(consumer::seek);
    }
}