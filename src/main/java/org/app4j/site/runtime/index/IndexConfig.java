package org.app4j.site.runtime.index;

/**
 * @author chi
 */
public interface IndexConfig {
    <T> Index<?> index(String name);

    <T> Index<T> createIndex(String name, Class<T> type, IndexLoader<T> indexLoader);
}
