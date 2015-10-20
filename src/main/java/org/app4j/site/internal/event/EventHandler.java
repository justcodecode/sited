package org.app4j.site.internal.event;

/**
 * @author chi
 */
public interface EventHandler<T> {
    void on(Event<T> event);
}
