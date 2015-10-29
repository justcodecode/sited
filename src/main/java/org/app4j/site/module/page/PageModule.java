package org.app4j.site.module.page;

import com.google.common.collect.Maps;
import org.app4j.site.Module;
import org.app4j.site.Site;
import org.app4j.site.internal.cache.service.DiskCache;
import org.app4j.site.internal.index.IndexModule;
import org.app4j.site.internal.index.service.Index;
import org.app4j.site.internal.template.web.admin.TemplateRESTController;
import org.app4j.site.internal.track.TrackModule;
import org.app4j.site.module.file.FileModule;
import org.app4j.site.module.page.processor.PagePaginationAttrProcessor;
import org.app4j.site.module.page.service.PageIndexService;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.page.service.SitemapService;
import org.app4j.site.module.page.service.codec.PageCodec;
import org.app4j.site.module.page.variable.PageVariable;
import org.app4j.site.module.page.variable.RequestVariable;
import org.app4j.site.module.page.variable.SiteVariable;
import org.app4j.site.module.page.variable.VariableConfig;
import org.app4j.site.module.page.web.PageHandler;
import org.app4j.site.module.page.web.SitemapController;
import org.app4j.site.module.page.web.admin.AdminPageRESTController;
import org.app4j.site.module.page.web.admin.AdminSitemapRESTController;
import org.app4j.site.module.user.UserModule;
import org.app4j.site.web.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public class PageModule extends Module implements PageConfig {
    private final Logger logger = LoggerFactory.getLogger(PageModule.class);
    private final VariableConfig variableConfig = new VariableConfig();

    public PageModule(Site site) {
        super(site);

        dependencies.add(IndexModule.class);
        dependencies.add(TrackModule.class);
        dependencies.add(UserModule.class);
        dependencies.add(FileModule.class);
    }

    @Override
    public void configure() throws Exception {
        bind(PageConfig.class).to(this).export();

        database().codecs().add(new PageCodec());

        PageService pageService = new PageService(database().get());
        bind(PageService.class).to(pageService);

        DiskCache sitemapDiskCache = cache().createDiskCache("sitemap", Integer.MAX_VALUE, TimeUnit.DAYS);
        SitemapService sitemapService = new SitemapService(site.baseURL(), pageService,
            sitemapDiskCache);
        bind(SitemapService.class).to(sitemapService);

        Index<Page> index = index().createIndex("page", Page.class, pageService.dumper());
        PageIndexService pageIndexService = new PageIndexService(index);
        bind(PageIndexService.class).to(pageIndexService);

        template().dialect()
            .add(new PagePaginationAttrProcessor(template().dialect(), site.baseURL()));


        PageHandler pageHandler = new PageHandler(variableConfig, template());
        route().get("/*", pageHandler);
        route().get("/", pageHandler);

        SitemapController sitemapController = new SitemapController(sitemapDiskCache);
        route().get("/sitemap.xml", sitemapController::sitemap);
        route().get("/sitemap/*", sitemapController::sitemap);


        variables()
            .addGlobalVariable("page", new PageVariable(pageService))
            .addGlobalVariable("request", new RequestVariable())
            .addGlobalVariable("site", new SiteVariable(site));


        if (site.isAdminEnabled()) {
            configureAdmin(pageService, pageIndexService, sitemapService);
        }
    }

    void configureAdmin(PageService pageService, PageIndexService pageIndexService, SitemapService sitemapService) {
        AdminPageRESTController adminPageRESTController = new AdminPageRESTController(pageService, pageIndexService, event());
        admin().route()
            .get("/admin/api/site", request -> {
                Map<String, Object> site = Maps.newHashMap();
                site.put("host", "");
                return Response.bean(site);
            })
            .get("/admin/api/page/", adminPageRESTController::findPages)
            .post("/admin/api/page", adminPageRESTController::createPage)
            .get("/admin/api/page/:id", adminPageRESTController::getPage)
            .put("/admin/api/page/:id", adminPageRESTController::updatePage)
            .delete("/admin/api/page/:id", adminPageRESTController::deletePage)
            .get("/admin/api/page/rebuild-index", adminPageRESTController::rebuildIndex);

        AdminSitemapRESTController adminSitemapRESTController = new AdminSitemapRESTController(sitemapService, event());
        admin().route().get("/admin/api/page/rebuild-sitemap", adminSitemapRESTController::rebuildSitemap);

        TemplateRESTController templateRESTController = new TemplateRESTController(template());
        admin().route().get("/admin/api/page/git/pull", templateRESTController::pull);
    }

    @Override
    public Optional<Page> get(String path) {
        throw new Error("not implement");
    }

    @Override
    public VariableConfig variables() {
        return variableConfig;
    }
}
