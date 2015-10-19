package org.app4j.site.internal.cache;

import org.app4j.site.internal.cache.service.DiskCache;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public interface CacheConfig {
    <T> Cache<T> cache(String name);

    <T> Cache<T> createCache(String name, Class<T> type, long expireTime, TimeUnit timeUnit);

    Cache<InputStream> diskCache(String name);

    DiskCache createDiskCache(String name, long expireTime, TimeUnit timeUnit);
}
