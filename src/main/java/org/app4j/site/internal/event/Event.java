package org.app4j.site.internal.event;

import com.google.common.base.Preconditions;
import org.app4j.site.util.JSON;
import org.app4j.site.util.Value;

import java.util.Map;

/**
 * @author chi
 */
public class Event<T> {
    public final T target;
    private final Map<String, Object> context;

    public Event(T target, Map<String, Object> context) {
        Preconditions.checkNotNull(target, "target can't be null");
        this.target = target;
        this.context = context;
    }

    public <K> Value<K> param(String key, Class<K> type) {
        if (context.containsKey(key)) {
            return new Value<>(key, JSON.mapper().convertValue(context.get(key), type));
        }
        return new Value<>(key, null);
    }

    public Value<String> param(String key) {
        return param(key, String.class);
    }
}
