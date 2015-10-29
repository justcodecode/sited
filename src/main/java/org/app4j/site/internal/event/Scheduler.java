package org.app4j.site.internal.event;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public interface Scheduler {
    Scheduler execute(Task task);

    Scheduler schedule(Date startTime, int interval, TimeUnit unit, Task task);

    ExecutorService pool();
}
