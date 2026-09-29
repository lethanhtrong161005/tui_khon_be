package com.tuikhon.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuikhon.service.RedisService;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link RedisService} wrapping Spring RedisTemplate.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    /**
     * Spring Data Redis template instance.
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Jackson object mapper instance.
     */
    private final ObjectMapper objectMapper;

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(value)) {
            return null;
        }
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        return objectMapper.convertValue(value, clazz);
    }

    @Override
    public String get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return Objects.nonNull(value) ? value.toString() : null;
    }

    @Override
    public boolean delete(String key) {
        Boolean result = redisTemplate.delete(key);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public boolean hasKey(String key) {
        Boolean result = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public long getExpire(String key, TimeUnit unit) {
        Long expire = redisTemplate.getExpire(key, unit);
        return Objects.nonNull(expire) ? expire : -1;
    }

    @Override
    public boolean expire(String key, long timeout, TimeUnit unit) {
        Boolean result = redisTemplate.expire(key, timeout, unit);
        return Boolean.TRUE.equals(result);
    }
}
