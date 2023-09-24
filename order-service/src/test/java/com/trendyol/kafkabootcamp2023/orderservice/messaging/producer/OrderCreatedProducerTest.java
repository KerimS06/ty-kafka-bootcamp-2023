package com.trendyol.kafkabootcamp2023.orderservice.messaging.producer;

import com.trendyol.kafkabootcamp2023.orderservice.messaging.producer.created.OrderCreatedProducer;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderType;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderCreatedProducerTest {

    @InjectMocks
    OrderCreatedProducer orderCreatedProducer;

    @Mock
    KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    public void it_should_produce_order_created_event() {
        //given
        ReflectionTestUtils.setField(orderCreatedProducer, "topic", "order-created-topic");

        var orderCreatedMessage = new OrderCreatedMessage();
        orderCreatedMessage.setOrderId(100L);
        orderCreatedMessage.setCustomerId(123L);
        orderCreatedMessage.setType(OrderType.CORE);

        //when
        orderCreatedProducer.produce(orderCreatedMessage);

        //then
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(kafkaTemplate).send(messageArgumentCaptor.capture());
        Message<OrderCreatedMessage> message = messageArgumentCaptor.getValue();
        assertThat(message.getPayload().getOrderId()).isEqualTo(100L);
        assertThat(message.getPayload().getCustomerId()).isEqualTo(123L);
        assertThat(message.getPayload().getType()).isEqualTo(OrderType.CORE);
    }
}