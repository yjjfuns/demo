package com.example.demo.mapper;

import com.example.demo.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    int insert(Order order);
}