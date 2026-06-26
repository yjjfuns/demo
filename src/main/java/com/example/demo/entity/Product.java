package com.example.demo.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String name;
    private Integer stock;
    private BigDecimal price;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}