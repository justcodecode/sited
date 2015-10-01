package org.app4j.site.runtime.event;

/**
 * @author chi
 */
public interface EventListener<T> {
    void on(Event<T> event);
}
