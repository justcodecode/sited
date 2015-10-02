package org.app4j.site.runtime.cache;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public interface CacheConfig {
    <T> Cache<T> cache(String name);

    <T> CacheConfig createCache(String name, Class<T> type, long expireTime, TimeUnit timeUnit);

    Cache<InputStream> diskCache(String name);

    CacheConfig createDiskCache(String name, long expireTime, TimeUnit timeUnit);
}
