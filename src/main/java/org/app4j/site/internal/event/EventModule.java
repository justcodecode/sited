package org.app4j.site.internal.event;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.app4j.site.Site;
import org.app4j.site.internal.InternalModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class EventModule extends InternalModule implements EventConfig {
    private final Map<Class<?>, List<EventHandler<?>>> listeners = Maps.newHashMap();
    private final Logger logger = LoggerFactory.getLogger(EventModule.class);
    private final Scheduler scheduler = new SchedulerImpl();


    public EventModule(Site site) {
        super(site);
    }

    @Override
    protected void configure() throws Exception {
        bind(EventConfig.class).to(this).export();
    }

    @Override
    public synchronized EventConfig on(Class<?> targetType, EventHandler eventHandler) {
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
        List<EventHandler<?>> handlers = listeners.get(event.target.getClass());
        if (handlers.isEmpty()) {
            return false;
        }

        scheduler.execute(new Task("handle-" + event) {
            @Override
            public void run() {
                handlers.forEach(handler -> handler.on((Event) event));
            }
        });
        return true;
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }
}
