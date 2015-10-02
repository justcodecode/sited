package org.app4j.site.runtime.variable;

import org.app4j.site.Scope;

/**
 * @author chi
 */
public interface Variable<T> {
    T eval(VariableRef ref, Scope scope);
}
