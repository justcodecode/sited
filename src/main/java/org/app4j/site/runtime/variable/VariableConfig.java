package org.app4j.site.runtime.variable;

import org.app4j.site.Scope;

/**
 * @author chi
 */
public interface VariableConfig {
    <T> VariableConfig add(String name, Variable<T> variable);

    <T> T eval(VariableRef ref, Scope scope);
}
