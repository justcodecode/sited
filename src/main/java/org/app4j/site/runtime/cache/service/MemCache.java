package org.app4j.site.runtime.cache.service;

import com.google.common.cache.CacheBuilder;
import org.app4j.site.runtime.cache.Cache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public class MemCache<T> implements Cache<T> {
    private final com.google.common.cache.Cache<String, T> cache;

    public MemCache(long expireTime, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder().expireAfterWrite(expireTime, timeUnit).build();
    }

    @Override
    public Optional<T> get(String key) {
        T value = cache.getIfPresent(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(value);
    }

    @Override
    public Cache<T> put(String key, T value) {
        cache.put(key, value);
        return this;
    }
}
