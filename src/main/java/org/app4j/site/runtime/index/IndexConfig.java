package org.app4j.site.runtime.index;

import org.app4j.site.runtime.index.service.Index;
import org.app4j.site.runtime.index.service.IndexLoader;

/**
 * @author chi
 */
public interface IndexConfig {
    <T> Index<?> index(String name);

    <T> Index<T> createIndex(String name, Class<T> type, IndexLoader<T> indexLoader);
}
