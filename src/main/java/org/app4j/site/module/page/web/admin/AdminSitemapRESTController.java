package org.app4j.site.module.page.web.admin;

import org.app4j.site.internal.event.EventConfig;
import org.app4j.site.internal.event.Task;
import org.app4j.site.module.page.service.SitemapService;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

/**
 * @author chi
 */
public class AdminSitemapRESTController {
    private final SitemapService sitemapService;
    private final EventConfig eventConfig;

    public AdminSitemapRESTController(SitemapService sitemapService, EventConfig eventConfig) {
        this.sitemapService = sitemapService;
        this.eventConfig = eventConfig;
    }

    public Response rebuildSitemap(Request request) {
        eventConfig.scheduler().execute(new Task("rebuild-sitemap") {
            @Override
            public void run() {
                sitemapService.rebuild();
            }
        });
        return Response.empty();
    }
}
