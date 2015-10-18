package org.app4j.site.module.page.variable;

import org.app4j.site.util.JSON;
import org.app4j.site.util.Value;

import java.util.Map;

/**
 * @author chi
 */
public class VariableRef {
    public final String name;
    public final String as;
    private final Map<String, Object> arguments;

    public VariableRef(String name, String as, Map<String, Object> arguments) {
        this.name = name;
        this.as = as;
        this.arguments = arguments;
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
