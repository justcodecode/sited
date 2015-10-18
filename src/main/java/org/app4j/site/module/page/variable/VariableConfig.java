package org.app4j.site.module.page.variable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.app4j.site.Scope;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class VariableConfig {
    private final Map<String, Variable<?>> variables = Maps.newHashMap();
    private final Set<String> globalVariables = Sets.newHashSet();

    public <T> VariableConfig addVariable(String name, Variable<T> variable) {
        return add(name, variable, false);
    }

    public <T> VariableConfig addGlobalVariable(String name, Variable<T> variable) {
        return add(name, variable, true);
    }

    private <T> VariableConfig add(String name, Variable<T> variable, boolean global) {
        Preconditions.checkState(!variables.containsKey(name), "variable %s exists", name);
        variables.put(name, variable);

        if (global) {
            globalVariables.add(name);
        }
        return this;
    }

    public List<VariableDef> globalVariables() {
        return variables.entrySet().stream()
            .filter(stringVariableEntry -> globalVariables.contains(stringVariableEntry.getKey()))
            .map(entry -> {
                VariableDef variableDef = new VariableDef();
                variableDef.name = entry.getKey();
                variableDef.description = entry.getValue().getClass().getName();
                variableDef.global = true;
                return variableDef;
            })
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T> T eval(VariableRef variableRef, Scope scope) {
        Preconditions.checkState(variables.containsKey(variableRef.name), "missing variable %s", variableRef.name);
        return (T) variables.get(variableRef.name).eval(variableRef, scope);
    }
}
