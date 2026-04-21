package com.example.shopflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopflow.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
