package org.app4j.site.runtime.event;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.app4j.site.runtime.InternalModule;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class EventModule extends InternalModule implements EventConfig {
    private final Map<Class<?>, List<EventHandler<?>>> listeners = Maps.newHashMap();
    private final Scheduler scheduler = new Scheduler();

    public Scheduler scheduler() {
        return scheduler;
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
        List<EventHandler<?>> handlers = listeners.get(event.target().getClass());
        if (handlers.isEmpty()) {
            return false;
        }

        scheduler.execute(new Task("handle-" + event.target()) {
            @Override
            public void run() {
                handlers.forEach(handler -> handler.on((Event) event));
            }
        });
        return true;
    }
}
