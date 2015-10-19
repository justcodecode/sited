package org.app4j.site.internal.database;

/**
 * @author chi
 */
public interface Pageable<T> extends Iterable<T> {
    long total();

    long offset();
}
