package org.app4j.site.module.page.admin;

import org.app4j.site.Site;
import org.app4j.site.module.page.domain.Page;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.page.web.PageHandler;
import org.app4j.site.util.JSON;
import org.app4j.site.web.Request;

import java.util.Optional;

/**
 * @author chi
 */
public class AdminPagePreviewHandler extends PageHandler {
    public AdminPagePreviewHandler(Site site, PageService pageService) {
        super(site, pageService);
    }

    @Override
    protected Optional<Page> page(String path, Request request) {
        String page = request.query("page").get();
        return Optional.of(JSON.parse(page, Page.class));
    }
}
