package com.example.shopflow.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.shopflow.entity.Product;
import com.example.shopflow.exception.BusinessException;
import com.example.shopflow.exception.ResourceNotFoundException;
import com.example.shopflow.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public void delete(Long id) {

        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }

        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Produto não pode ser deletado pois está vinculado a um pedido");
        }
    }
}
