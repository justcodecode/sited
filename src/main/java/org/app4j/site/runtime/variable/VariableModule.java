package org.app4j.site.runtime.variable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.app4j.site.Scope;
import org.app4j.site.runtime.InternalModule;

import java.util.Map;

/**
 * @author chi
 */
public class VariableModule extends InternalModule implements VariableConfig {
    private final Map<String, Variable<?>> variables = Maps.newHashMap();

    @Override
    protected void configure() throws Exception {
        bind(VariableConfig.class).to(this).export();
    }

    @Override
    public <T> VariableConfig add(String name, Variable<T> variable) {
        Preconditions.checkState(!variables.containsKey(name), "variable %s exists", name);
        variables.put(name, variable);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T eval(VariableRef ref, Scope scope) {
        Preconditions.checkState(variables.containsKey(ref.name()), "missing variable %s", ref.name());
        return (T) variables.get(ref.name()).eval(ref, scope);
    }
}
