package org.app4j.site.runtime.cache;

import java.util.Optional;

/**
 * @author chi
 */
public interface Cache<T> {
    Optional<T> get(String key);

    Cache<T> put(String key, T value);
}
