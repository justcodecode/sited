package org.app4j.site;

/**
 * @author chi
 */
public interface Scope {
    <T> T require(Class<T> type);

    <T> T require(Class<T> type, String qualifier);
}
