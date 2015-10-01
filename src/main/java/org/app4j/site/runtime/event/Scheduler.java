package org.app4j.site.runtime.event;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author chi
 */
public class Scheduler {
    private static final int MAX_JOBS = 10000;
    private final ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final EvictingQueue<Job> history = EvictingQueue.create(1000);

    public void execute(Job job) {
        while (pool.getTaskCount() - pool.getCompletedTaskCount() > MAX_JOBS) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new Error(e);
            }
        }

        pool.execute(job);
        history.add(job);
    }

    public List<Job> history() {
        return Lists.newArrayList(history);
    }
}
