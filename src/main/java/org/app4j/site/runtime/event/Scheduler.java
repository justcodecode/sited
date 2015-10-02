package org.app4j.site.runtime.event;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public class Scheduler {
    private final Logger logger = LoggerFactory.getLogger(Scheduler.class);
    private final ThreadPoolExecutor pool = new ThreadPoolExecutor(20, 20, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(10000));
    private final EvictingQueue<TaskHistory> histories = EvictingQueue.create(1000);

    public void execute(Task task) {
        TaskHistory history = new TaskHistory(task);
        histories.add(history);
        pool.execute(() -> {
            try {
                task.setProgress(0);
                task.setState(Task.State.PROCESSING);
                task.run();
                task.setProgress(100);
                task.setState(Task.State.FINISHED);
            } catch (Exception e) {
                logger.error("task {} failed", task.name(), e);
                task.setState(Task.State.FAILED);
                task.setProgress(100);
            }
        });
    }

    public List<TaskHistory> histories() {
        return Lists.newArrayList(histories);
    }
}
