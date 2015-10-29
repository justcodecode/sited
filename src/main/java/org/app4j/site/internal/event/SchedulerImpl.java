package org.app4j.site.internal.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public class SchedulerImpl implements Scheduler {
    private final Logger logger = LoggerFactory.getLogger(SchedulerImpl.class);
    private final ExecutorService pool = Executors.newFixedThreadPool(10);
    private final Timer timer = new Timer();

    @Override
    public Scheduler execute(Task task) {
        pool.execute(task);
        return this;
    }

    @Override
    public Scheduler schedule(Date startTime, int interval, TimeUnit unit, Task task) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logger.info("run task %s", task.name);
                task.run();
            }
        }, 0, unit.toMillis(interval));
        return this;
    }

    @Override
    public ExecutorService pool() {
        return pool;
    }
}
