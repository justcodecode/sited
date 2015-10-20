package org.app4j.site.internal.event;

/**
 * @author chi
 */
public interface Scheduler {
    Scheduler execute(Runnable runnable);

    Scheduler schedule(String cron, Runnable runnable);
}
