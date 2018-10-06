//package org.yuanmeng.zwolfserver.redis;
//
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * @author wangtonghe
// * @since 2018/10/6 07:56
// */
//@Component
//public class RedisCacheTemplate {
//
//    @Resource
//    private RedisTemplate<String, String> redisTemplate;
//
//    public String get(String key) {
//        return redisTemplate.opsForValue().get(key);
//    }
//
//    public void set(String key, String value) {
//        redisTemplate.opsForValue().set(key, value);
//    }
//}
