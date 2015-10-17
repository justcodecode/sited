package org.app4j.site.runtime.scheduler;

/**
 * @author chi
 */
public interface EventHandler<T> {
    void on(Event<T> event);
}
