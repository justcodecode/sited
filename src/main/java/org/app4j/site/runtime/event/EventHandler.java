package org.app4j.site.runtime.event;

/**
 * @author chi
 */
public interface EventHandler<T> {
    void on(Event<T> event);
}
