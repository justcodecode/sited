package org.app4j.site.runtime.scheduler;

import com.google.common.base.Preconditions;
import org.app4j.site.util.JSON;
import org.app4j.site.util.Value;

import java.util.Map;

/**
 * @author chi
 */
public class Event<T> {
    private final T target;
    private final Map<String, Object> parameters;

    public Event(T target, Map<String, Object> parameters) {
        Preconditions.checkNotNull(target, "target can't be null");
        this.target = target;
        this.parameters = parameters;
    }

    public T target() {
        return target;
    }

    public <K> Value<K> param(String key, Class<K> type) {
        if (parameters.containsKey(key)) {
            return new Value<>(key, JSON.mapper().convertValue(parameters.get(key), type));
        }
        return new Value<>(key, null);
    }

    public Value<String> param(String key) {
        return param(key, String.class);
    }
}
