package com.trendyol.kafkabootcamp2023.orderservice.messaging.consumer.created;

import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.OrderCreatedMessageRetryProducer;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import com.trendyol.kafkabootcamp2023.orderservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderCreatedMessageErrorConsumerTest {

    @InjectMocks
    OrderCreatedMessageErrorConsumer orderCreatedMessageErrorConsumer;

    @Mock
    OrderCreatedMessageRetryProducer retryProducer;

    @Mock
    NotificationService notificationService;

    @Test
    public void it_should_consume_order_created_message() {
        //given
        var message = new OrderCreatedMessage();
        message.setOrderId(100L);
        message.setCustomerId(123L);

        //when
        orderCreatedMessageErrorConsumer.consumeError(message, 1, 1L, "topic");

        //then
        verify(notificationService).sendNotification(message);
    }

    @Test
    void it_should_produce_error_message_when_exception_thrown() {
        //given
        var message = new OrderCreatedMessage();
        message.setOrderId(100L);
        message.setCustomerId(123L);

        doThrow(new RuntimeException()).when(notificationService).sendNotification(any());

        //when
        orderCreatedMessageErrorConsumer.consumeError(message, 1, 1L, "topic");

        //then
        verify(retryProducer).sendErrorMessage(message);
    }

}