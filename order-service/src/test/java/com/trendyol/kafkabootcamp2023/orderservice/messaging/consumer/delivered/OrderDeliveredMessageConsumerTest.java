package com.trendyol.kafkabootcamp2023.orderservice.messaging.consumer.delivered;

import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.OrderCreatedMessageRetryProducer;
import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.OrderDeliveredMessageRetryProducer;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.consumer.created.OrderCreatedMessageConsumer;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderDeliveredMessage;
import com.trendyol.kafkabootcamp2023.orderservice.service.NotificationService;
import com.trendyol.kafkabootcamp2023.orderservice.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderDeliveredMessageConsumerTest {

    @InjectMocks
    OrderDeliveredMessageConsumer orderDeliveredMessageConsumer;

    @Mock
    OrderDeliveredMessageRetryProducer retryProducer;

    @Mock
    PaymentService paymentService;

    @Test
    public void it_should_consume_order_created_message() {
        //given
        var message = new OrderDeliveredMessage();
        message.setOrderId(100L);

        //when
        orderDeliveredMessageConsumer.consume(message, 1, 1L, "topic");

        //then
        verify(paymentService).createPayment(message);
    }

    @Test
    void it_should_produce_retry_message_when_exception_thrown() {
        //given
        var message = new OrderDeliveredMessage();
        message.setOrderId(100L);

        doThrow(new RuntimeException()).when(paymentService).createPayment(any());

        //when
        orderDeliveredMessageConsumer.consume(message, 1, 1L, "topic");

        //then
        verify(retryProducer).sendRetryMessage(message);
    }

}