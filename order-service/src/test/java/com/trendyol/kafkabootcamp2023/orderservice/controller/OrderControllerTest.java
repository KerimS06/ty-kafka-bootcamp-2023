package com.trendyol.kafkabootcamp2023.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderStatus;
import com.trendyol.kafkabootcamp2023.orderservice.model.OrderType;
import com.trendyol.kafkabootcamp2023.orderservice.model.request.OrderCreateRequest;
import com.trendyol.kafkabootcamp2023.orderservice.model.request.OrderStatusUpdateRequest;
import com.trendyol.kafkabootcamp2023.orderservice.model.response.OrderResponse;
import com.trendyol.kafkabootcamp2023.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @Autowired
    MockMvc orderController;

    @MockBean
    OrderService orderService;

    JacksonTester<OrderCreateRequest> orderCreateRequestJacksonTester;
    JacksonTester<OrderStatusUpdateRequest> orderStatusUpdateRequestJacksonTester;

    @BeforeEach
    public void before() {
        JacksonTester.initFields(this, ObjectMapper::new);
    }

    @Test
    public void it_should_create_order() throws Exception {
        //given
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setSellerId(123L);
        orderCreateRequest.setCustomerId(100L);
        orderCreateRequest.setOrderType(OrderType.CORE);
        orderCreateRequest.setOrderAmount(BigDecimal.TEN);
        orderCreateRequest.setCustomerId(100L);

        String body = orderCreateRequestJacksonTester.write(orderCreateRequest).getJson();

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(1L);
        orderResponse.setSellerId(123L);
        orderResponse.setCustomerId(100L);
        orderResponse.setOrderType(OrderType.CORE);
        orderResponse.setOrderStatus(OrderStatus.CREATED);

        when(orderService.createOrder(any(OrderCreateRequest.class))).thenReturn(orderResponse);

        //when
        ResultActions resultActions = orderController.perform(
                post("/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body));

        //then
        resultActions.andExpect(status().isCreated());
        resultActions
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.sellerId").value(123L))
                .andExpect(jsonPath("$.customerId").value(100L))
                .andExpect(jsonPath("$.orderType").value("CORE"))
                .andExpect(jsonPath("$.orderStatus").value("CREATED"));

        ArgumentCaptor<OrderCreateRequest> orderCreateCaptor = ArgumentCaptor.forClass(OrderCreateRequest.class);
        verify(orderService).createOrder(orderCreateCaptor.capture());
        OrderCreateRequest request = orderCreateCaptor.getValue();
        assertThat(request).usingRecursiveComparison()
                .isEqualTo(orderCreateRequest);

    }
    @Test
    public void it_should_update_order_status() throws Exception {
        //arrange
        OrderStatusUpdateRequest orderStatusUpdateRequest = new OrderStatusUpdateRequest();
        orderStatusUpdateRequest.setOrderId(1L);
        orderStatusUpdateRequest.setOrderStatus(OrderStatus.DELIVERED);

        String body = orderStatusUpdateRequestJacksonTester.write(orderStatusUpdateRequest).getJson();

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(1L);
        orderResponse.setSellerId(123L);
        orderResponse.setCustomerId(100L);
        orderResponse.setOrderType(OrderType.CORE);
        orderResponse.setOrderStatus(OrderStatus.DELIVERED);
        orderResponse.setOrderAmount(BigDecimal.TEN);

        when(orderService.updateOrderStatus(orderStatusUpdateRequest)).thenReturn(orderResponse);

        //act
        ResultActions resultActions = orderController.perform(
                put("/orders/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body));

        //assert
        resultActions.andExpect(status().isOk());
        resultActions
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.sellerId").value(123L))
                .andExpect(jsonPath("$.customerId").value(100L))
                .andExpect(jsonPath("$.orderType").value("CORE"))
                .andExpect(jsonPath("$.orderStatus").value("DELIVERED"))
                .andExpect(jsonPath("$.orderAmount").value(BigDecimal.TEN));

        ArgumentCaptor<OrderStatusUpdateRequest> updateRequestCaptor = ArgumentCaptor.forClass(OrderStatusUpdateRequest.class);
        verify(orderService).updateOrderStatus(updateRequestCaptor.capture());
        assertThat(updateRequestCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(orderStatusUpdateRequest);
    }
}