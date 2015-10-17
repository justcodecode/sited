package org.app4j.site.runtime.scheduler;


import org.app4j.site.runtime.event.Event;
import org.app4j.site.runtime.event.EventHandler;
import org.app4j.site.runtime.event.Scheduler;

/**
 * @author chi
 */
public interface SchedulerConfig {
    <T> SchedulerConfig on(Class<T> event, EventHandler<T> eventHandler);

    <T> boolean trigger(Event<T> event);

    Scheduler scheduler();
}
