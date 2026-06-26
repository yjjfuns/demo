Demo
一个轻量级 Spring Boot 实战项目，覆盖 MVC、依赖注入、事务、AOP、拦截器、MyBatis、MySQL 索引、Redis 缓存、Redisson 分布式锁、RocketMQ 消息队列等核心知识点./


1.Spring MVC	OrderController 使用 @RestController、@PostMapping 处理 HTTP 请求
2.依赖注入	OrderServiceImpl 通过 @RequiredArgsConstructor 构造器注入 Mapper、RedissonClient、RocketMQTemplate 等
3.声明式事务	createOrder 方法上的 @Transactional，保证扣库存与下单原子性
4.AOP	ServiceLogAspect 环绕通知记录 Service 层执行时间
5.拦截器	LogInterceptor 实现 HandlerInterceptor，统计请求耗时
6.MyBatis + 基本 SQL	ProductMapper.xml 中的 SELECT/UPDATE，OrderMapper.xml 中的 INSERT
7.索引	order 表建 product_id、user_id、status 索引
8.Redis 数据结构	OrderServiceImpl 使用 RedisTemplate 的 opsForHash 缓存商品对象
9.Redisson 分布式锁	RLock 对商品 ID 加锁，防止并发超卖
10.RocketMQ 生产者/消费者	rocketMQTemplate.convertAndSend 发送订单消息，OrderMessageConsumer 监听并处理
