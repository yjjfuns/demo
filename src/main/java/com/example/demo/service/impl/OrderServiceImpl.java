package com.example.demo.service.impl;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final RedissonClient redissonClient;
    private final RocketMQTemplate rocketMQTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(Long productId, Long userId, int quantity) {
        String lockKey = "lock:product:" + productId;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (!lock.tryLock(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("系统繁忙，请稍后重试");
            }
            Product product = getProductFromCache(productId);
            if (product == null) {
                product = productMapper.selectById(productId);
                if (product != null) {
                    // 写入 Redis 缓存（String + Hash）
                    cacheProduct(product);
                }
            }
            if (product == null) {
                throw new RuntimeException("商品不存在");
            }
            if (product.getStock() < quantity) {
                throw new RuntimeException("库存不足");
            }

            productMapper.deductStock(productId, quantity);


            BigDecimal amount = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            Order order = new Order();
            order.setProductId(productId);
            order.setUserId(userId);
            order.setQuantity(quantity);
            order.setAmount(amount);
            order.setStatus("CREATED");
            orderMapper.insert(order);
            product.setStock(product.getStock() - quantity);
            cacheProduct(product);
            rocketMQTemplate.convertAndSend("order-topic", order.getId());
            log.info("订单创建成功，ID: {}, 消息已发送", order.getId());
            return order.getId();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("加锁被中断", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
    // 从 Redis 读取商品（Hash 结构）
    private Product getProductFromCache(Long productId) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries("product:" + productId);
        if (entries == null || entries.isEmpty()) {
            return null;
        }
        Product product = new Product();
        product.setId(productId);
        product.setName((String) entries.get("name"));
        product.setStock((Integer) entries.get("stock"));
        Object priceObj = entries.get("price");
        if (priceObj != null) {
            product.setPrice(new BigDecimal(priceObj.toString()));
        }
        return product;
    }

    // 缓存商品到 Redis
    private void cacheProduct(Product product) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", product.getName());
        map.put("stock", product.getStock());
        map.put("price", product.getPrice().doubleValue());
        redisTemplate.opsForHash().putAll("product:" + product.getId(), map);
        redisTemplate.expire("product:" + product.getId(), 10, TimeUnit.MINUTES);
    }
}