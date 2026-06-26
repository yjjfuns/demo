package com.example.demo.service;

public interface OrderService {
    Long createOrder(Long productId, Long userId, int quantity);
}