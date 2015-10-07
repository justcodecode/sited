package org.app4j.site.runtime.cache.service;

import com.google.common.io.ByteStreams;
import org.app4j.site.runtime.cache.Cache;
import org.app4j.site.util.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public class DiskCache implements Cache<InputStream> {
    private final File dir;
    private final long expireTime;
    private final TimeUnit timeUnit;

    public DiskCache(File dir, long expireTime, TimeUnit timeUnit) {
        this.dir = dir;
        this.expireTime = expireTime;
        this.timeUnit = timeUnit;
    }

    @Override
    public Optional<InputStream> get(String key) {
        File file = new File(dir, key);

        if (file.exists()) {
            try {
                BasicFileAttributes attr = java.nio.file.Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                if (System.currentTimeMillis() - attr.creationTime().toMillis() < timeUnit.toMillis(expireTime)) {
                    return Optional.of(new FileInputStream(file));
                }
            } catch (IOException e) {
                throw new Error(e);
            }
        }
        return Optional.empty();
    }

    @Override
    public Cache<InputStream> put(String key, InputStream value) {
        File file = new File(dir, key);
        Files.createParentDirs(file);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            ByteStreams.copy(value, outputStream);
            return this;
        } catch (IOException e) {
            throw new Error(e);
        }
    }
}
