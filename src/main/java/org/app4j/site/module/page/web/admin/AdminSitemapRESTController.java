package org.app4j.site.module.page.web.admin;

import org.app4j.site.module.page.service.SitemapService;
import org.app4j.site.runtime.event.EventConfig;
import org.app4j.site.runtime.event.Task;
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
        eventConfig.scheduler().execute(new Task("build-site-map") {
            @Override
            public void run() {
                sitemapService.rebuild();
            }
        });
        return Response.empty();
    }
}
