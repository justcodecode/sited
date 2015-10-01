package org.app4j.site.runtime.cache;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import org.app4j.site.runtime.InternalModule;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public class CacheConfig extends InternalModule {
    private final Map<String, Cache<String, ?>> caches = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    public <T> Cache<String, T> cache(String name) {
        Preconditions.checkState(caches.containsKey(name), "missing cache %s", name);
        return (Cache<String, T>) caches.get(name);
    }


    public <T> Cache<String, T> register(String name, long time, TimeUnit timeUnit) {
        Cache<String, T> cache = CacheBuilder.newBuilder().expireAfterWrite(time, timeUnit).build();
        caches.put(name, cache);
        return cache;
    }

    @Override
    protected void configure() throws Exception {
        bind(CacheConfig.class).to(this).export();
    }

    @Override
    protected String name() {
        return "cache";
    }
}
