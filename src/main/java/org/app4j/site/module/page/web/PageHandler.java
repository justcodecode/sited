package org.app4j.site.module.page.web;

import com.google.common.collect.Maps;
import org.app4j.site.Site;
import org.app4j.site.module.page.domain.Page;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.page.web.api.impl.DirectoryPageObjectImpl;
import org.app4j.site.module.page.web.api.impl.RequestPageObjectImpl;
import org.app4j.site.module.page.web.api.impl.SitePageObjectImpl;
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
    protected final PageService pageService;
    protected final Site site;

    public PageHandler(Site site, PageService pageService) {
        this.pageService = pageService;
        this.site = site;
    }

    @Override
    public Response handle(Request request) throws IOException {
        PageRef pageRef = new PageRef(request);
        Optional<Page> pageOptional = page(pageRef);

        if (pageOptional.isPresent()) {
            Page page = pageOptional.get();
            Map<String, Object> context = Maps.newHashMap();
            context.put("__page__", new DirectoryPageObjectImpl(page, pageService, pageRef.pageNumber()));
            context.put("__request__", new RequestPageObjectImpl(request));
            context.put("__site__", new SitePageObjectImpl(site, pageService));

            return Response.template(page.getTemplate(), context);
        } else {
            Map<String, Object> context = Maps.newHashMap();
            context.put("__request__", new RequestPageObjectImpl(request));
            context.put("__site__", new SitePageObjectImpl(site, pageService));

            return Response.template(pageRef.template(), context);
        }
    }

    protected Optional<Page> page(PageRef pageRef) {
        return pageService.findByPath(pageRef.path());
    }
}
