package org.app4j.site.internal.event;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chi
 */
public class SchedulerImpl implements Scheduler {
    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    @Override
    public Scheduler execute(Task task) {
        pool.execute(task);
        return this;
    }

    @Override
    public Scheduler schedule(LocalDateTime time, ChronoUnit unit, Task task) {
        throw new Error("not implement");
    }
}
