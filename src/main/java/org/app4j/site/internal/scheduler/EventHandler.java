package org.app4j.site.internal.scheduler;

/**
 * @author chi
 */
public interface EventHandler<T> {
    void on(Event<T> event);
}
