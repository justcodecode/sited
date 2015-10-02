package org.app4j.site.module.page.web.admin;

import org.app4j.site.Site;
import org.app4j.site.module.page.domain.Page;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.page.web.PageHandler;
import org.app4j.site.module.page.web.PageRef;
import org.app4j.site.util.JSON;

import java.util.Optional;

/**
 * @author chi
 */
public class AdminPagePreviewHandler extends PageHandler {
    public AdminPagePreviewHandler(Site site, PageService pageService) {
        super(site, pageService);
    }

    @Override
    protected Optional<Page> page(PageRef pageRef) {
//        String page = request.query("page").get();
        return Optional.of(JSON.parse("", Page.class));
    }
}
