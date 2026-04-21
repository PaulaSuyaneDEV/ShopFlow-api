package com.example.shopflow.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {

    private List<CreateOrderItemRequest> items;
}