package com.trendyol.kafkabootcamp2023.orderservice.messaging.producer;

import com.trendyol.kafkabootcamp2023.orderservice.config.kafka.consumer.retry.OrderCreatedMessageRetryProducer;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderCreatedMessageRetryProducerTest {

    @InjectMocks
    private OrderCreatedMessageRetryProducer retryProducer;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;


    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(retryProducer,
                "retryTopic",
                "retry_topic");
        ReflectionTestUtils.setField(retryProducer,
                "errorTopic",
                "error_topic");
        ReflectionTestUtils.setField(retryProducer,
                "retryCount",
                3);
    }

    @Test
    void it_should_produce_event_to_retry_topic() {
        //given
        var message = new OrderCreatedMessage();
        message.setOrderId(100L);
        message.setCustomerId(123L);

        //when
        retryProducer.sendRetryMessage(message);

        //then
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(kafkaTemplate).send(messageArgumentCaptor.capture());
        Message<OrderCreatedMessage> retryMessage = messageArgumentCaptor.getValue();
        assertThat(retryMessage.getPayload()).usingRecursiveComparison().isEqualTo(message);
        assertThat(retryMessage.getHeaders().get(KafkaHeaders.TOPIC)).isEqualTo("retry_topic");
        assertThat(retryMessage.getHeaders().get(KafkaHeaders.MESSAGE_KEY)).isEqualTo("100");
    }

    @Test
    void it_should_produce_event_to_error_topic() {
        //given
        var message = new OrderCreatedMessage();
        message.setOrderId(100L);
        message.setCustomerId(123L);

        //when
        retryProducer.sendErrorMessage(message);

        //then
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(kafkaTemplate).send(messageArgumentCaptor.capture());
        Message<OrderCreatedMessage> retryMessage = messageArgumentCaptor.getValue();
        assertThat(retryMessage.getPayload()).usingRecursiveComparison().isEqualTo(message);
        assertThat(retryMessage.getHeaders().get(KafkaHeaders.TOPIC)).isEqualTo("error_topic");
        assertThat(retryMessage.getHeaders().get(KafkaHeaders.MESSAGE_KEY)).isEqualTo("100");
    }
}