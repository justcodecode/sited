package org.app4j.site.module.page.web;

import com.google.common.collect.Maps;
import org.app4j.site.Site;
import org.app4j.site.module.page.domain.Page;
import org.app4j.site.module.page.service.PageIndexService;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.page.template.impl.DirectoryObjectImpl;
import org.app4j.site.module.page.template.impl.PageObjectImpl;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class PageHandler implements Handler {
    protected final Site site;
    protected final PageService pageService;
    protected final PageIndexService pageIndexService;

    public PageHandler(Site site, PageService pageService, PageIndexService pageIndexService) {
        this.pageService = pageService;
        this.site = site;
        this.pageIndexService = pageIndexService;
    }

    @Override
    public Response handle(Request request) throws IOException {
        PageRef pageRef = new PageRef(request);
        Optional<Page> pageOptional = page(pageRef);
        if (pageOptional.isPresent()) {
            Page page = pageOptional.get();
            Map<String, Object> context = Maps.newHashMap();
            context.put("request", request);
            if (pageRef.isDirectory()) {
                context.put("page", new DirectoryObjectImpl(page, pageService, pageIndexService, pageRef.pageNumber()));
            } else {
                context.put("page", new PageObjectImpl(page, pageService, pageIndexService));
            }
            return Response.template(page.getTemplate(), context);
        } else {
            Map<String, Object> context = Maps.newHashMap();
            context.put("request", request);
            return Response.template(pageRef.template(), context);
        }
    }

    protected Optional<Page> page(PageRef pageRef) {
        return pageService.findByPath(pageRef.path());
    }
}
