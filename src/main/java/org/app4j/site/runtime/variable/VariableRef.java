package org.app4j.site.runtime.variable;

import org.app4j.site.util.JSON;
import org.app4j.site.util.Value;

import java.util.Map;

/**
 * @author chi
 */
public class VariableRef {
    private final String name;
    private final Map<String, Object> arguments;

    public VariableRef(String name, Map<String, Object> parameters) {
        this.name = name;
        this.arguments = parameters;
    }

    public String name() {
        return name;
    }

    public <K> Value<K> param(String key, Class<K> type) {
        if (arguments.containsKey(key)) {
            return new Value<>(key, JSON.mapper().convertValue(arguments.get(key), type));
        }
        return new Value<>(key, null);
    }

    public Value<String> param(String key) {
        return param(key, String.class);
    }
}
