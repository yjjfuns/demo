package com.example.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLogAspect {

    @Around("execution(* com.example.demo.service..*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String method = joinPoint.getSignature().toShortString();
        log.info("开始执行: {}", method);
        Object result = joinPoint.proceed();
        long time = System.currentTimeMillis() - start;
        log.info("执行完成: {} 耗时: {}ms", method, time);
        return result;
    }
}