package com.tuikhon.service;

import java.util.concurrent.TimeUnit;

/**
 * Reusable global service interface for application-wide Redis key-value operations.
 */
public interface RedisService {

    /**
     * Sets key-value pair without expiration.
     *
     * @param key   Redis key string.
     * @param value Value object.
     */
    void set(String key, Object value);

    /**
     * Sets key-value pair with expiration time.
     *
     * @param key     Redis key string.
     * @param value   Value object.
     * @param timeout Expiration duration value.
     * @param unit    Time unit.
     */
    void set(String key, Object value, long timeout, TimeUnit unit);

    /**
     * Gets value from Redis converted to target class type.
     *
     * @param <T>   Target object type.
     * @param key   Redis key string.
     * @param clazz Target Class type.
     * @return Object converted to target type or null if key does not exist.
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * Gets string representation of value stored at key.
     *
     * @param key Redis key string.
     * @return String value or null if key does not exist.
     */
    String get(String key);

    /**
     * Deletes key from Redis.
     *
     * @param key Redis key string.
     * @return True if key was removed.
     */
    boolean delete(String key);

    /**
     * Checks if key exists in Redis.
     *
     * @param key Redis key string.
     * @return True if key exists.
     */
    boolean hasKey(String key);

    /**
     * Gets remaining expiration time of key.
     *
     * @param key  Redis key string.
     * @param unit Time unit.
     * @return Remaining duration or -1 if no expiration set.
     */
    long getExpire(String key, TimeUnit unit);

    /**
     * Sets expiration time for an existing key.
     *
     * @param key     Redis key string.
     * @param timeout Expiration duration value.
     * @param unit    Time unit.
     * @return True if expiration was set.
     */
    boolean expire(String key, long timeout, TimeUnit unit);
}
