package com.trendyol.kafkabootcamp2023.orderservice.messaging.consumer.delivered;

import com.trendyol.kafkabootcamp2023.orderservice.messaging.consumer.created.OrderCreatedMessageRetryConsumer;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderDeliveredMessage;
import com.trendyol.kafkabootcamp2023.orderservice.service.NotificationService;
import com.trendyol.kafkabootcamp2023.orderservice.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderDeliveredMessageRetryConsumerTest {

    @InjectMocks
    OrderDeliveredMessageRetryConsumer orderDeliveredMessageRetryConsumer;

    @Mock
    PaymentService paymentService;

    @Test
    public void it_should_consume_order_created_message() {
        //given
        var message = new OrderDeliveredMessage();
        message.setOrderId(100L);
        //when
        orderDeliveredMessageRetryConsumer.consumeRetry(message, 1, 1L, "topic");

        //then
        verify(paymentService).createPayment(message);
    }

}