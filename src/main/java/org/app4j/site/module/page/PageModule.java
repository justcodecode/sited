package org.app4j.site.module.page;

import org.app4j.site.Module;
import org.app4j.site.Site;
import org.app4j.site.module.file.FileModule;
import org.app4j.site.module.page.admin.AdminPagePreviewHandler;
import org.app4j.site.module.page.admin.AdminPageRESTController;
import org.app4j.site.module.page.processor.PagePaginationAttrProcessor;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.page.service.codec.PageCodec;
import org.app4j.site.module.page.web.MobilePageHandler;
import org.app4j.site.module.page.web.PageHandler;
import org.app4j.site.module.track.TrackModule;
import org.app4j.site.module.user.UserModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author chi
 */
public class PageModule extends Module {
    private final Logger logger = LoggerFactory.getLogger(PageModule.class);

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(Site.class, TrackModule.class, UserModule.class, FileModule.class);
    }

    @Override
    public void configure() throws Exception {
        database().codecs().add(new PageCodec());
        PageService pageService = new PageService(database().get());
        bind(PageService.class).to(pageService).export();

        site().template().dialect()
                .add(new PagePaginationAttrProcessor(template().dialect(), site().baseURL()));

//        SiteMapService siteMapService = new SiteMapService(siteEngine.baseUrl())
//                .setCache(site().cache().register("sitemap", 1, TimeUnit.DAYS))
//                .setPageService(pageService)
//                .setTrackingService(require(TrackingService.class));

        PageHandler pageHandler = new PageHandler(site(), pageService);
        route().get("/*", pageHandler);
        route().get("/", pageHandler);

        MobilePageHandler mobilePageHandler = new MobilePageHandler(site(), pageService);
        route().get("/m/*", mobilePageHandler);
        route().get("/m/", mobilePageHandler);

        AdminPageRESTController adminPageRESTController = new AdminPageRESTController(pageService);
        admin().get("/admin/api/page/", adminPageRESTController::findPages);
        admin().post("/admin/api/page", adminPageRESTController::createPage);
        admin().get("/admin/api/page/:id", adminPageRESTController::getPage);
        admin().put("/admin/api/page/:id", adminPageRESTController::updatePage);
        admin().delete("/admin/api/page/:id", adminPageRESTController::deletePage);

        AdminPagePreviewHandler adminPagePreviewHandler = new AdminPagePreviewHandler(site(), pageService);
        admin().post("/admin/draft/*", adminPagePreviewHandler);
    }

    @Override
    protected String name() {
        return "name";
    }
}
