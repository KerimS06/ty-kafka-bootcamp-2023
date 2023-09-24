package com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry;

import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public final class RetryTemplateFactory {

    private RetryTemplateFactory() {
    }

    public static RetryTemplate getSimpleFixedRetryTemplate(Long backOffPeriod, Integer maxAttempts) {
        var backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(backOffPeriod);

        var retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(maxAttempts);

        var retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }
}
