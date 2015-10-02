package org.app4j.site.runtime.variable.processor;

import org.app4j.site.Binding;
import org.app4j.site.DefaultScope;

/**
 * @author chi
 */
public class TemplateScope extends DefaultScope {
    public TemplateScope(DefaultScope parent) {
        super(parent);
    }

    @Override
    public <T> Binding.Named<T> bind(Class<T> type) {
        return super.bind(type);
    }
}
