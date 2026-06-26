package com.example.demo.controller;

import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Map<String, Object> createOrder(@RequestParam Long productId,
                                           @RequestParam Long userId,
                                           @RequestParam int quantity) {
        Long orderId = orderService.createOrder(productId, userId, quantity);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "下单成功");
        result.put("orderId", orderId);
        return result;
    }
}