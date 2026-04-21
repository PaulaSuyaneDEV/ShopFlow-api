package com.example.shopflow.controller;


import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.shopflow.entity.Product;
import com.example.shopflow.service.ProductService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Criar um novo produto")
    public Product create(@RequestBody Product product) {
        return productService.create(product);
    }

    @GetMapping
    @Operation(summary = "Listar todos os produtos")
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public Product findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto por ID")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}