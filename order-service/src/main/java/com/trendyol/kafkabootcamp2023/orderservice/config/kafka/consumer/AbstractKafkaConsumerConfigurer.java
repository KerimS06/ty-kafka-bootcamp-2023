package com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer;

import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.GlobalKafkaRetryListenerSupport;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.producer.RetryProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.retry.RecoveryCallback;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.Objects;

import static org.springframework.kafka.listener.adapter.RetryingMessageListenerAdapter.CONTEXT_RECORD;

public abstract class AbstractKafkaConsumerConfigurer<T> {

    protected final KafkaConsumerFactorySupport kafkaConsumerFactorySupport;
    protected final KafkaConsumerProperties propertySource;
    private final GlobalKafkaRetryListenerSupport retryListenerSupport;
    protected final RetryProducer<T> retryProducer;

    protected AbstractKafkaConsumerConfigurer(KafkaConsumerFactorySupport kafkaConsumerFactorySupport,
                                              KafkaConsumerProperties propertySource,
                                              GlobalKafkaRetryListenerSupport retryListenerSupport,
                                              RetryProducer<T> retryProducer) {
        Assert.notNull(kafkaConsumerFactorySupport, "param kafkaConsumerFactorySupport could not be empty");
        Assert.notNull(propertySource, "param propertySource could not be empty");

        this.kafkaConsumerFactorySupport = kafkaConsumerFactorySupport;
        this.propertySource = propertySource;
        this.retryListenerSupport = retryListenerSupport;
        this.retryProducer = retryProducer;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected Class<T> getPayloadClass() {
        return (Class<T>) ((ParameterizedType) ((Class) getClass().getGenericSuperclass()).getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected ConcurrentKafkaListenerContainerFactory<String, T> createConsumerListenerFactory() {
        return kafkaConsumerFactorySupport.createListenerContainerFactory(
                getPayloadClass(),
                getConsumerProps(),
                consumerEnabled()
        );
    }

    protected ConcurrentKafkaListenerContainerFactory<String, T> createRetryListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = kafkaConsumerFactorySupport.createListenerContainerFactory(
                getPayloadClass(),
                getRetryProps(),
                retryEnabled()
        );

        var retryTemplate = retryProducer.getRetryTemplate();
        retryTemplate.registerListener(retryListenerSupport);

        factory.setRetryTemplate(retryTemplate);
        factory.setRecoveryCallback(getRecoveryCallback());
        return factory;
    }

    @SuppressWarnings({"unchecked"})
    public RecoveryCallback<T> getRecoveryCallback() {
        return retryContext -> {
            ConsumerRecord<String, T> consumerRecord = (ConsumerRecord<String, T>) retryContext.getAttribute(CONTEXT_RECORD);

            Assert.notNull(consumerRecord, "param consumerRecord could not be empty");

            var message = consumerRecord.value();
            retryProducer.sendErrorMessage(message);
            return message;
        };
    }

    protected ConcurrentKafkaListenerContainerFactory<String, T> createErrorListenerFactory() {
        return kafkaConsumerFactorySupport.createListenerContainerFactory(
                getPayloadClass(),
                getErrorProps(),
                errorEnabled()
        );
    }

    protected Map<String, Object> getConsumerProps() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertySource.getBootstrapServers(),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propertySource.getAutoOffsetReset(),
                ConsumerConfig.MAX_POLL_RECORDS_CONFIG, propertySource.getMaxPollRecords(),
                ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, propertySource.getMaxPollInterval(),
                ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, propertySource.getRequestTimeout(),
                ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, propertySource.getSessionTimeout()
        );
    }

    protected Map<String, Object> getRetryProps() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertySource.getRetry().getBootstrapServers(),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propertySource.getRetry().getAutoOffsetReset(),
                ConsumerConfig.MAX_POLL_RECORDS_CONFIG, propertySource.getRetry().getMaxPollRecords(),
                ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, propertySource.getRetry().getMaxPollInterval(),
                ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, propertySource.getRetry().getRequestTimeout(),
                ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, propertySource.getRetry().getSessionTimeout()
        );
    }

    protected Map<String, Object> getErrorProps() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertySource.getError().getBootstrapServers(),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propertySource.getError().getAutoOffsetReset(),
                ConsumerConfig.MAX_POLL_RECORDS_CONFIG, propertySource.getError().getMaxPollRecords(),
                ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, propertySource.getError().getMaxPollInterval(),
                ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, propertySource.getError().getRequestTimeout(),
                ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, propertySource.getError().getSessionTimeout()
        );
    }


    protected boolean consumerEnabled() {
        return Objects.requireNonNull(propertySource).isEnabled();
    }

    protected boolean retryEnabled() {
        return Objects.requireNonNull(propertySource.getRetry()).isEnabled();
    }

    protected boolean errorEnabled() {
        return Objects.requireNonNull(propertySource.getError()).isEnabled();
    }

}
