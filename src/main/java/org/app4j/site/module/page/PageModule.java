package org.app4j.site.module.page;

import org.app4j.site.Module;
import org.app4j.site.module.file.FileModule;
import org.app4j.site.module.page.processor.PagePaginationAttrProcessor;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.page.service.codec.PageCodec;
import org.app4j.site.module.page.web.PageHandler;
import org.app4j.site.module.page.web.admin.AdminPagePreviewHandler;
import org.app4j.site.module.page.web.admin.AdminPageRESTController;
import org.app4j.site.module.track.TrackModule;
import org.app4j.site.module.user.UserModule;
import org.app4j.site.runtime.template.FolderResourceRepository;
import org.app4j.site.runtime.template.web.AssetsHandler;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author chi
 */
public class PageModule extends Module {
    private final Logger logger = LoggerFactory.getLogger(PageModule.class);

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(TrackModule.class, UserModule.class, FileModule.class);
    }

    @Override
    public void configure() throws Exception {
        database().codecs().add(new PageCodec());
        PageService pageService = new PageService(database().get());
        bind(PageService.class).to(pageService).export();


        File templateDir = new File(property("template.dir").orElse(site().dir("template").getAbsolutePath()).get());
        if (!templateDir.exists()) {
            templateDir.mkdirs();
        }
        FolderResourceRepository resourceRepository = new FolderResourceRepository(templateDir, 1000);
        template().add(resourceRepository);
        template().dialect()
                .add(new PagePaginationAttrProcessor(template().dialect(), site().baseURL()));

        template().assets().add(resourceRepository);
        route().get("/assets/*", new AssetsHandler(template().assets()));

//        SiteMapService siteMapService = new SiteMapService(siteEngine.baseUrl())
//                .setCache(site().cache().register("sitemap", 1, TimeUnit.DAYS))
//                .setPageService(pageService)
//                .setTrackingService(require(TrackingService.class));

        PageHandler pageHandler = new PageHandler(site(), pageService);
        route().get("/*", pageHandler);
        route().get("/", pageHandler);

        AdminPageRESTController adminPageRESTController = new AdminPageRESTController(pageService);
        AdminPagePreviewHandler adminPagePreviewHandler = new AdminPagePreviewHandler(site(), pageService);
        admin().route()
                .get("/admin/api/page/", adminPageRESTController::findPages)
                .post("/admin/api/page", adminPageRESTController::createPage)
                .get("/admin/api/page/:id", adminPageRESTController::getPage)
                .put("/admin/api/page/:id", adminPageRESTController::updatePage)
                .delete("/admin/api/page/:id", adminPageRESTController::deletePage)
                .post("/admin/draft/*", adminPagePreviewHandler);

        error().on(NotFoundException.class, (request, e) -> Response.empty().setStatusCode(404));
        error().on(Throwable.class, (request, e) -> Response.empty().setStatusCode(500));
    }

}
