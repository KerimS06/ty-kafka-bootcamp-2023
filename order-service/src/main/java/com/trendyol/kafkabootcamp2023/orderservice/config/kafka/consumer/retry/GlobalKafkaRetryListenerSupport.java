package com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.springframework.kafka.listener.adapter.RetryingMessageListenerAdapter.CONTEXT_RECORD;

@Component
public class GlobalKafkaRetryListenerSupport
        extends RetryListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalKafkaRetryListenerSupport.class);

    @Override
    @SuppressWarnings({"rawtypes"})
    public <T, E extends Throwable> void onError(
            RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {

        LOGGER.error(throwable.getLocalizedMessage(), throwable);

        var retryCount = context.getRetryCount();
        var consumerRecord = (ConsumerRecord) context.getAttribute(CONTEXT_RECORD);

        Assert.notNull(consumerRecord, "param consumerRecord could not be empty");

        var topic = consumerRecord.topic();
        var offset = consumerRecord.offset();
        var partition = consumerRecord.partition();
        var key = consumerRecord.key();
        var message = consumerRecord.value();

        LOGGER.info("Kafka message is retried. Retry Count: {}, Topic: {}, Partition: {}, Offset: {}, Key: {}, Message: {}",
                retryCount, topic, partition, offset, key, message);
    }
}
