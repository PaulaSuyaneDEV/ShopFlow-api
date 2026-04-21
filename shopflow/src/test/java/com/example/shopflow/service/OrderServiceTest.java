package com.example.shopflow.service;

import com.example.shopflow.entity.Order;
import com.example.shopflow.entity.OrderItem;
import com.example.shopflow.entity.Product;
import com.example.shopflow.repository.OrderRepository;
import com.example.shopflow.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateOrderAndCalculateTotalCorrectly() {

        Product product = Product.builder()
                .id(1L)
                .name("Produto Teste")
                .price(new BigDecimal("10.00"))
                .stock(10)
                .build();

        OrderItem item = OrderItem.builder()
                .product(product)
                .quantity(2)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.createOrder(List.of(item));

        assertNotNull(order);
        assertEquals(new BigDecimal("20.00"), order.getTotal());
        assertEquals(1, order.getItems().size());

        OrderItem savedItem = order.getItems().get(0);
        assertEquals(product.getPrice(), savedItem.getPrice());
        assertEquals(order, savedItem.getOrder());

        verify(productRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        OrderItem item = OrderItem.builder()
                .product(Product.builder().id(1L).build())
                .quantity(2)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(List.of(item));
        });

        assertEquals("Produto não encontrado", exception.getMessage());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCalculateTotalWithMultipleItems() {

        Product p1 = Product.builder().id(1L).price(new BigDecimal("10.00")).build();
        Product p2 = Product.builder().id(2L).price(new BigDecimal("5.00")).build();

        OrderItem item1 = OrderItem.builder().product(p1).quantity(2).build(); 
        OrderItem item2 = OrderItem.builder().product(p2).quantity(3).build(); 

        when(productRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(p2));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.createOrder(List.of(item1, item2));

        assertEquals(new BigDecimal("35.00"), order.getTotal());
        assertEquals(2, order.getItems().size());
    }

    @Test
    void shouldHandleEmptyItemList() {

        when(orderRepository.save(any(Order.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.createOrder(List.of());

        assertNotNull(order);
        assertEquals(BigDecimal.ZERO, order.getTotal());
        assertTrue(order.getItems().isEmpty());
    }
}