package org.app4j.site.internal.index;

import org.app4j.site.internal.database.Dumper;
import org.app4j.site.internal.index.service.Index;

/**
 * @author chi
 */
public interface IndexConfig {
    <T> Index<?> index(String name);

    <T> Index<T> createIndex(String name, Class<T> type, Dumper<T> dumper);
}
