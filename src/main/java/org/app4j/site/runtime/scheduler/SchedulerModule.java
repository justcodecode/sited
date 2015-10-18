package org.app4j.site.runtime.scheduler;


import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.app4j.site.Site;
import org.app4j.site.runtime.SiteModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public class SchedulerModule extends SiteModule implements SchedulerConfig {
    private final Map<Class<?>, List<EventHandler<?>>> listeners = Maps.newHashMap();
    private final Logger logger = LoggerFactory.getLogger(SchedulerModule.class);
    private final ThreadPoolExecutor pool = new ThreadPoolExecutor(20, 20, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(10000));
    private final EvictingQueue<TaskHistory> histories = EvictingQueue.create(1000);

    public SchedulerModule(Site site) {
        super(site);
    }

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

    @Override
    protected void configure() throws Exception {
        bind(SchedulerConfig.class).to(this).export();
    }

    @Override
    public synchronized SchedulerConfig on(Class<?> targetType, EventHandler eventHandler) {
        if (listeners.containsKey(targetType)) {
            listeners.get(targetType).add(eventHandler);
        } else {
            listeners.put(targetType, Lists.newArrayList(eventHandler));
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean trigger(Event<?> event) {
        List<EventHandler<?>> handlers = listeners.get(event.target().getClass());
        if (handlers.isEmpty()) {
            return false;
        }

        pool.execute(new Task("handle-" + event.target()) {
            @Override
            public void run() {
                handlers.forEach(handler -> handler.on((Event) event));
            }
        });
        return true;
    }

    @Override
    public void execute(Runnable task) {

    }
}
