package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.example.util.MapperUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RedisHashComponent {

    private final RedisTemplate<String,Object> redisTemplate;

    public RedisHashComponent(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void hSet(String key, Object hashKey, Object value){
        redisTemplate.opsForHash().put(key,hashKey, MapperUtils.objectMapper(value, Map.class));
    }

    public Object hGet(String key, Object hashKey){
        return redisTemplate.opsForHash().get(key,hashKey);
    }

    public List<Object> hValues(String key){
        return redisTemplate.opsForHash().values(key);
    }
}
