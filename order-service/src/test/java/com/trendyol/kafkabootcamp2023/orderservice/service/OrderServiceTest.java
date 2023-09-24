package com.trendyol.kafkabootcamp2023.orderservice.service;

import com.trendyol.kafkabootcamp2023.orderservice.domain.Order;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderStatus;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderType;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.message.OrderCreatedMessage;
import com.trendyol.kafkabootcamp2023.orderservice.model.request.OrderCreateRequest;
import com.trendyol.kafkabootcamp2023.orderservice.messaging.producer.created.OrderCreatedProducer;
import com.trendyol.kafkabootcamp2023.orderservice.repository.OrderRepository;
import com.trendyol.kafkabootcamp2023.orderservice.util.Clock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderCreatedProducer orderCreatedProducer;

    @Test
    public void it_should_create_order() {
        //given
        Clock.freeze();
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setSellerId(123L);
        orderCreateRequest.setOrderType(OrderType.CORE);
        orderCreateRequest.setOrderAmount(BigDecimal.TEN);
        orderCreateRequest.setCustomerId(100L);

        Order savedOrder = orderCreateRequest.toOrder();
        savedOrder.setId(1L);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        //when
        var orderResponse = orderService.createOrder(orderCreateRequest);

        //then
        assertThat(orderResponse.getId()).isEqualTo(1L);
        assertThat(orderResponse.getSellerId()).isEqualTo(123L);
        assertThat(orderResponse.getCustomerId()).isEqualTo(100L);
        assertThat(orderResponse.getOrderType()).isEqualTo(OrderType.CORE);
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.CREATED);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order actualOrder = orderCaptor.getValue();
        assertThat(actualOrder.getOrderStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(actualOrder.getOrderDate()).isEqualTo(Clock.now().toDate());
        assertThat(actualOrder.getOrderType()).isEqualTo(OrderType.CORE);
        assertThat(actualOrder.getOrderAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(actualOrder.getCustomerId()).isEqualTo(100L);
        assertThat(actualOrder.getSellerId()).isEqualTo(123L);

        var orderCreatedEventCaptor = ArgumentCaptor.forClass(OrderCreatedMessage.class);
        verify(orderCreatedProducer).produce(orderCreatedEventCaptor.capture());
        OrderCreatedMessage event = orderCreatedEventCaptor.getValue();
        assertThat(event.getCustomerId()).isEqualTo(orderCreateRequest.getCustomerId());
        assertThat(event.getOrderId()).isEqualTo(savedOrder.getId());
        assertThat(event.getType()).isEqualTo(savedOrder.getOrderType());
        Clock.unfreeze();
    }

}
