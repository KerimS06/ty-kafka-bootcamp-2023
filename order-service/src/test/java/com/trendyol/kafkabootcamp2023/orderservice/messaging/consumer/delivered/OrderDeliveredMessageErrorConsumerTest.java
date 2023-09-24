package com.trendyol.kafkabootcamp2023.orderservice.messaging.consumer.delivered;

import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.OrderCreatedMessageRetryProducer;
import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.OrderDeliveredMessageRetryProducer;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.consumer.created.OrderCreatedMessageErrorConsumer;
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
class OrderDeliveredMessageErrorConsumerTest {

    @InjectMocks
    OrderDeliveredMessageErrorConsumer orderDeliveredMessageErrorConsumer;

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
        orderDeliveredMessageErrorConsumer.consumeError(message, 1, 1L, "topic");

        //then
        verify(paymentService).createPayment(message);
    }

    @Test
    void it_should_produce_error_message_when_exception_thrown() {
        //given
        var message = new OrderDeliveredMessage();
        message.setOrderId(100L);

        doThrow(new RuntimeException()).when(paymentService).createPayment(any());

        //when
        orderDeliveredMessageErrorConsumer.consumeError(message, 1, 1L, "topic");

        //then
        verify(retryProducer).sendErrorMessage(message);
    }

}