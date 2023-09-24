package com.trendyol.kafkabootcamp2023.orderservice.controller;

import com.trendyol.kafkabootcamp2023.orderservice.model.request.OrderStatusUpdateRequest;
import com.trendyol.kafkabootcamp2023.orderservice.model.request.OrderCreateRequest;
import com.trendyol.kafkabootcamp2023.orderservice.model.response.OrderResponse;
import com.trendyol.kafkabootcamp2023.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        var orderResponse = orderService.createOrder(orderCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<OrderResponse> updateOrderStatus(@RequestBody OrderStatusUpdateRequest request) {
        var orderResponse = orderService.updateOrderStatus(request);
        return ResponseEntity.ok().body(orderResponse);
    }
}
