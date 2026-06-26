package com.example.demo.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "order-topic", consumerGroup = "demo-consumer-group")
public class OrderMessageConsumer implements RocketMQListener<Long> {

    @Override
    public void onMessage(Long orderId) {
        log.info("收到订单消息，订单ID: {}，开始后续处理", orderId);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("订单ID: {} 处理完成", orderId);
    }
}