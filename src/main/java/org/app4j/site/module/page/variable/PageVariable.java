package org.app4j.site.module.page.variable;

import org.app4j.site.Scope;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.runtime.variable.Variable;
import org.app4j.site.runtime.variable.VariableRef;
import org.app4j.site.web.Request;

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
        Request request = scope.require(Request.class);


        return null;
    }
}
