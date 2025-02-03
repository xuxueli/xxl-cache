//package com.xxl.cache.core.util.tmp;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.redis.connection.MessageListener;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.TimeUnit;
//
///**
// * redis cache
// *
// * @author xuxueli 2025-02-03
// */
//public class RedisCache {
//    private static Logger logger = LoggerFactory.getLogger(RedisCache.class);
//
//    /*private static RedisCache redisCache;
//    public static RedisCache instance() {
//        if (redisCache == null) {
//            throw new RuntimeException("RedisTool instance empty.");
//        }
//        return redisCache;
//    }*/
//
//    public RedisCache(RedisTemplate<String, Object> redisTemplateForRedisTool, RedisMessageListenerContainer redisMessageListenerContainer) {
//        this.redisTemplateForRedisTool = redisTemplateForRedisTool;
//        this.redisMessageListenerContainer = redisMessageListenerContainer;
//
//        //redisCache = this;
//    }
//
//    private RedisTemplate<String, Object> redisTemplateForRedisTool;
//    private RedisMessageListenerContainer redisMessageListenerContainer;
//
//    private final ConcurrentMap<String, MessageListener> listeners = new ConcurrentHashMap<>();
//    private final ConcurrentMap<String, ChannelTopic> topics = new ConcurrentHashMap<>();
//
//    // 设置值
//    public void setValue(String key, Object value) {
//        redisTemplateForRedisTool.opsForValue().set(key, value);
//    }
//
//    // 设置值并设置过期时间
//    public void setValue(String key, Object value, long timeout, TimeUnit unit) {
//        redisTemplateForRedisTool.opsForValue().set(key, value, timeout, unit);
//    }
//
//    // 获取值
//    public Object getValue(String key) {
//        return redisTemplateForRedisTool.opsForValue().get(key);
//    }
//
//    // 删除键
//    public void deleteKey(String key) {
//        redisTemplateForRedisTool.delete(key);
//    }
//
//    // 检查键是否存在
//    public boolean hasKey(String key) {
//        return redisTemplateForRedisTool.hasKey(key);
//    }
//
//    // 设置过期时间
//    public boolean expire(String key, long timeout, TimeUnit unit) {
//        return redisTemplateForRedisTool.expire(key, timeout, unit);
//    }
//
//    // 获取过期时间
//    public Long getExpire(String key, TimeUnit unit) {
//        return redisTemplateForRedisTool.getExpire(key, unit);
//    }
//
//    // 操作列表
//    public void leftPush(String key, Object value) {
//        redisTemplateForRedisTool.opsForList().leftPush(key, value);
//    }
//
//    public Object rightPop(String key) {
//        return redisTemplateForRedisTool.opsForList().rightPop(key);
//    }
//
//    // 操作集合
//    public void addSet(String key, Object... values) {
//        redisTemplateForRedisTool.opsForSet().add(key, values);
//    }
//
//    public boolean isMemberSet(String key, Object value) {
//        return redisTemplateForRedisTool.opsForSet().isMember(key, value);
//    }
//
//    // 操作哈希
//    public void putHash(String key, String hashKey, Object value) {
//        redisTemplateForRedisTool.opsForHash().put(key, hashKey, value);
//    }
//
//    public Object getHash(String key, String hashKey) {
//        return redisTemplateForRedisTool.opsForHash().get(key, hashKey);
//    }
//
//    // 操作ZSet
//    public void addZSet(String key, Object value, double score) {
//        redisTemplateForRedisTool.opsForZSet().add(key, value, score);
//    }
//
//    public Set<Object> rangeZSet(String key, long start, long end) {
//        return redisTemplateForRedisTool.opsForZSet().range(key, start, end);
//    }
//
//    // 发布消息
//    public void publish(String channel, String message) {
//        redisTemplateForRedisTool.convertAndSend(channel, message);
//    }
//
//    // 订阅消息
//    public void subscribe(String channel, MessageListener messageListener) {
//        ChannelTopic topic = new ChannelTopic(channel);
//        redisMessageListenerContainer.addMessageListener(messageListener, topic);
//
//        topics.put(channel, topic);
//        listeners.put(channel, messageListener);
//    }
//
//    // 取消订阅消息
//    public void unsubscribe(String channel) {
//        ChannelTopic topic = topics.get(channel);
//        MessageListener listener = listeners.get(channel);
//
//        if (topic != null && listener != null) {
//            redisMessageListenerContainer.removeMessageListener(listener, topic);
//
//            topics.remove(channel);
//            listeners.remove(channel);
//        }
//    }
//
//}
