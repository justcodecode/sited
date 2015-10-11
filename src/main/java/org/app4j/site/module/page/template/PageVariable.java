package org.app4j.site.module.page.template;

import org.app4j.site.Scope;
import org.app4j.site.module.page.domain.Page;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.page.template.impl.DirectoryObjectImpl;
import org.app4j.site.module.page.template.impl.PageObjectImpl;
import org.app4j.site.runtime.variable.Variable;
import org.app4j.site.runtime.variable.VariableRef;
import org.app4j.site.web.Request;

/**
 * @author chi
 */
public class PageVariable implements Variable<Object> {
    private final PageService pageService;

    public PageVariable(PageService pageService) {
        this.pageService = pageService;
    }

    @Override
    public Object eval(VariableRef ref, Scope scope) {
        Request request = scope.require(Request.class);
        String path = ref.param("path").isPresent()
                ? ref.param("path").get()
                : request.path();
        Page page = pageService.findByPath(path).get();



        return pageObject(page);
    }

    Object pageObject(Page page) {
        if (page.isDirectory()) {
            return new DirectoryObjectImpl(page, pageService, 0);
        } else {
            return new PageObjectImpl(page, pageService);
        }
    }
}
