package org.app4j.site.module.page.processor;

import org.app4j.site.Binding;
import org.app4j.site.ScopeImpl;

/**
 * @author chi
 */
public class TemplateScopeImpl extends ScopeImpl {
    public TemplateScopeImpl(ScopeImpl parent) {
        super(parent);
    }

    @Override
    public <T> Binding.Named<T> bind(Class<T> type) {
        return super.bind(type);
    }
}
