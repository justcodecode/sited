package org.app4j.site.module.page.variable;

import org.app4j.site.Scope;

/**
 * @author chi
 */
public interface Variable<T> {
    T eval(VariableRef variableRef, Scope scope);
}
