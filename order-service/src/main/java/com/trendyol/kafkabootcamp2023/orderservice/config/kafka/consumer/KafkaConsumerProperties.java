package com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer;

public class KafkaConsumerProperties {

    protected String bootstrapServers;
    protected String topic;
    protected String autoOffsetReset;
    protected boolean enabled;
    protected Integer maxPollRecords;
    protected Integer maxPollInterval;
    protected Integer requestTimeout;
    protected Integer sessionTimeout;
    protected KafkaConsumerProperties retry;
    protected KafkaConsumerProperties error;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public KafkaConsumerProperties getRetry() {
        return retry;
    }

    public void setRetry(KafkaConsumerProperties retry) {
        this.retry = retry;
    }

    public KafkaConsumerProperties getError() {
        return error;
    }

    public void setError(KafkaConsumerProperties error) {
        this.error = error;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public Integer getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(Integer maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public Integer getMaxPollInterval() {
        return maxPollInterval;
    }

    public void setMaxPollInterval(Integer maxPollInterval) {
        this.maxPollInterval = maxPollInterval;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}
