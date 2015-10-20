package org.app4j.site.internal.event;


/**
 * @author chi
 */
public interface EventConfig {
    EventConfig on(Class<?> event, EventHandler eventHandler);

    boolean trigger(Event<?> event);

    Scheduler scheduler();
}
