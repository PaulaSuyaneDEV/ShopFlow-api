package com.example.shopflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopflow.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
