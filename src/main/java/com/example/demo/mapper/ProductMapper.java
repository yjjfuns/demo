package com.example.demo.mapper;

import com.example.demo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {

    Product selectById(@Param("id") Long id);

    int deductStock(@Param("productId") Long productId, @Param("quantity") int quantity);
}