package org.app4j.site.runtime.event;

import java.util.Map;

/**
 * @author chi
 */
public class Event<T> {
    private final T target;
    private final String name;
    private final Map<String, Object> parameters;

    public Event(String name, T target, Map<String, Object> parameters) {
        this.name = name;
        this.target = target;
        this.parameters = parameters;
    }

    public T target() {
        return target;
    }

    public Class<T> type() {
        return (Class<T>) target.getClass();
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> parameters() {
        return parameters;
    }
}
