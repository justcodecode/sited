package org.app4j.site.module.page.web.admin;

import org.app4j.site.module.page.service.SitemapService;
import org.app4j.site.runtime.scheduler.SchedulerConfig;
import org.app4j.site.runtime.scheduler.Task;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

/**
 * @author chi
 */
public class AdminSitemapRESTController {
    private final SitemapService sitemapService;
    private final SchedulerConfig eventConfig;

    public AdminSitemapRESTController(SitemapService sitemapService, SchedulerConfig eventConfig) {
        this.sitemapService = sitemapService;
        this.eventConfig = eventConfig;
    }

    public Response rebuildSitemap(Request request) {
        eventConfig.execute(new Task("build-site-map") {
            @Override
            public void run() {
                sitemapService.rebuild();
            }
        });
        return Response.empty();
    }
}
