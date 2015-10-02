package org.app4j.site.runtime.event;


/**
 * @author chi
 */
public interface EventConfig {
    EventConfig on(Class<?> event, EventHandler eventHandler);

    boolean trigger(Event<?> event);

    Scheduler scheduler();
}
