package org.app4j.site.module.page.web;

import org.app4j.site.Site;
import org.app4j.site.module.page.domain.Page;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.runtime.assets.Resource;
import org.app4j.site.web.Request;

import java.util.Optional;

/**
 * @author chi
 */
public class MobilePageHandler extends PageHandler {
    public MobilePageHandler(Site site, PageService pageService) {
        super(site, pageService);
    }

    @Override
    protected Optional<Page> page(String path, Request request) {
        return pageService.findByPath(path.substring("/m".length()));
    }

    @Override
    protected Resource template(Page page) {
        return site.template().get("/m" + page.getTemplate());
    }
}
