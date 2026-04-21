package com.example.shopflow.controller;

import org.springframework.web.bind.annotation.*;

import com.example.shopflow.dto.CreateOrderRequest;
import com.example.shopflow.dto.OrderResponse;
import com.example.shopflow.entity.Order;
import com.example.shopflow.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor

public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Criar pedido com múltiplos itens")
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrderFromDTO(request);
        return orderService.toResponse(order);
    }
}