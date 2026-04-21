package com.example.shopflow.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Long id;
    private LocalDateTime createdAt;
    private BigDecimal total;
    private List<OrderItemResponse> items;
}