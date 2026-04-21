package com.example.shopflow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.shopflow.dto.CreateOrderRequest;
import com.example.shopflow.dto.OrderItemResponse;
import com.example.shopflow.dto.OrderResponse;
import com.example.shopflow.entity.Order;
import com.example.shopflow.entity.OrderItem;
import com.example.shopflow.entity.Product;
import com.example.shopflow.repository.OrderRepository;
import com.example.shopflow.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public Order createOrder(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
                throw new RuntimeException("Pedido deve conter ao menos um item");
        }

        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : items) {

                Product product = productRepository.findById(item.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

                BigDecimal itemTotal = product.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));

                item.setPrice(product.getPrice());
                item.setOrder(order);

                total = total.add(itemTotal);
        }

        order.setItems(items);
        order.setTotal(total);

        return orderRepository.save(order);
}


    public Order createOrderFromDTO(CreateOrderRequest request) {
        List<OrderItem> items = request.getItems().stream().map(itemDTO -> {

            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            return OrderItem.builder()
                    .product(product)
                    .quantity(itemDTO.getQuantity())
                    .build();

        }).toList();

        return createOrder(items);
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .total(order.getTotal())
                .items(items)
                .build();
    }
}