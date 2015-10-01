package org.app4j.site.module.page.web.api.impl;

import org.app4j.site.Site;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.page.web.api.DirectoryPageObject;
import org.app4j.site.module.page.web.api.IndicesPageObject;
import org.app4j.site.module.page.web.api.SitePageObject;
import org.app4j.site.module.page.web.api.TrackingPageObject;

import java.util.HashMap;

/**
 * @author chi
 */
public class SitePageObjectImpl extends HashMap<String, Object> implements SitePageObject {
    private final transient Site site;
    private final transient PageService pageService;

    public SitePageObjectImpl(Site site, PageService pageService) {
        this.site = site;
        this.pageService = pageService;
    }

    @Override
    public DirectoryPageObject root() {
        return new DirectoryPageObjectImpl(pageService.root(), pageService, 0);
    }

    @Override
    public IndicesPageObject indices() {
        return null;
    }

    @Override
    public TrackingPageObject tracking() {
        return null;
    }
}
