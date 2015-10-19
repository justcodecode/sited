package org.app4j.site.internal.scheduler;


/**
 * @author chi
 */
public interface SchedulerConfig {
    SchedulerConfig on(Class<?> event, EventHandler eventHandler);

    boolean trigger(Event<?> event);

    void execute(Runnable task);
}
