package org.app4j.site.internal.cache;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.app4j.site.Site;
import org.app4j.site.internal.InternalModule;
import org.app4j.site.internal.cache.service.DiskCache;
import org.app4j.site.internal.cache.service.MemCache;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public class CacheModule extends InternalModule implements CacheConfig {
    private final Map<String, Cache<?>> caches = Maps.newHashMap();
    private final File dir;

    public CacheModule(Site site) {
        super(site);
        this.dir = site.dir("cache");
    }

    @SuppressWarnings("unchecked")
    public <T> Cache<T> cache(String name) {
        Preconditions.checkState(caches.containsKey(name), "missing cache %s", name);
        return (Cache<T>) caches.get(name);
    }

    @Override
    public <T> Cache<T> createCache(String name, Class<T> type, long expireTime, TimeUnit timeUnit) {
        Preconditions.checkState(caches.containsKey(name), "cache %s already exists", name);
        MemCache<T> cache = new MemCache<>(expireTime, timeUnit);
        caches.put(name, cache);
        return cache;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Cache<InputStream> diskCache(String name) {
        Preconditions.checkState(!caches.containsKey(name), "missing disk cache %s", name);
        return (Cache<InputStream>) caches.get(name);
    }

    @Override
    public DiskCache createDiskCache(String name, long expireTime, TimeUnit timeUnit) {
        Preconditions.checkState(!caches.containsKey(name), "disk cache %s already exists", name);
        DiskCache cache = new DiskCache(new File(dir, name), expireTime, timeUnit);
        caches.put(name, cache);
        return cache;
    }

    @Override
    protected void configure() throws Exception {
        bind(CacheConfig.class).to(this).export();
    }
}
