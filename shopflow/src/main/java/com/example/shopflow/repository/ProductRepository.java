package com.example.shopflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopflow.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}