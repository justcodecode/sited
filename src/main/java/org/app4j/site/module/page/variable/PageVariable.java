package org.app4j.site.module.page.variable;

import com.google.common.base.Preconditions;
import org.app4j.site.Scope;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.runtime.variable.Variable;
import org.app4j.site.runtime.variable.VariableRef;

/**
 * @author chi
 */
public class PageVariable implements Variable<PageObject> {
    private final PageService pageService;

    public PageVariable(PageService pageService) {
        this.pageService = pageService;
    }

    @Override
    public PageObject eval(VariableRef ref, Scope scope) {
        Preconditions.checkNotNull(pageService);
        return null;
    }
}
