package org.app4j.site.runtime.database;

/**
 * @author chi
 */
public interface Pageable<T> extends Iterable<T> {
    long total();

    long offset();
}
