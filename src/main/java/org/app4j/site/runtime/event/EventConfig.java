package org.app4j.site.runtime.event;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.app4j.site.runtime.InternalModule;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class EventConfig extends InternalModule {
    private final Map<Class<?>, List<EventListener<?>>> listeners = Maps.newHashMap();
    private final Scheduler scheduler = new Scheduler();

    public <T> boolean pop(Event<T> event) {
        if (listeners.containsKey(event.type())) {
            for (EventListener eventListener : listeners.get(event.type())) {
                eventListener.on(event);
            }

            return true;
        }
        return false;
    }


    public <T> EventConfig addListener(Class<T> type, EventListener<T> eventListener) {
        if (listeners.containsKey(type)) {
            listeners.get(type).add(eventListener);
        } else {
            listeners.put(type, Lists.<EventListener<?>>newArrayList(eventListener));
        }
        return this;
    }

    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    protected void configure() throws Exception {
        bind(EventConfig.class).to(this).export();
    }

    @Override
    protected String name() {
        return "event";
    }
}
