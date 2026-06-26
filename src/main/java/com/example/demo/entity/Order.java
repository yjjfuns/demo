package com.example.demo.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private Long productId;
    private Long userId;
    private Integer quantity;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}