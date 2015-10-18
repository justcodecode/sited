package org.app4j.site.module.page;

import com.google.common.collect.Maps;
import org.app4j.site.Module;
import org.app4j.site.Site;
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
import org.app4j.site.runtime.cache.service.DiskCache;
import org.app4j.site.runtime.index.IndexModule;
import org.app4j.site.runtime.index.service.Index;
import org.app4j.site.runtime.template.service.TemplateRepository;
import org.app4j.site.runtime.track.TrackModule;
import org.app4j.site.util.Files;
import org.app4j.site.util.FolderResourceRepository;
import org.app4j.site.web.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
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
    }

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(IndexModule.class, TrackModule.class, UserModule.class, FileModule.class);
    }

    @Override
    public void configure() throws Exception {
        database().codecs().add(new PageCodec());

        PageService pageService = new PageService(database().get());
        bind(PageService.class).to(pageService);

        DiskCache sitemapDiskCache = cache().createDiskCache("sitemap", Integer.MAX_VALUE, TimeUnit.DAYS);
        SitemapService sitemapService = new SitemapService(site().baseURL(), pageService,
            sitemapDiskCache);
        bind(SitemapService.class).to(sitemapService);

        Index<Page> index = index().createIndex("page", Page.class, pageService.dumper());
        PageIndexService pageIndexService = new PageIndexService(index);
        bind(PageIndexService.class).to(pageIndexService);

        File templateDir = new File(property("site.web.dir").orElse(site().dir("web").getAbsolutePath()).get());
        Files.createDirIfNoneExists(templateDir);

        template()
            .add(new TemplateRepository(new FolderResourceRepository(templateDir)));

        template().dialect()
            .add(new PagePaginationAttrProcessor(template().dialect(), site().baseURL()));


        PageHandler pageHandler = new PageHandler(variableConfig);
        route().get("/*", pageHandler);
        route().get("/", pageHandler);

        SitemapController sitemapController = new SitemapController(sitemapDiskCache);
        route().get("/sitemap.xml", sitemapController::sitemap);
        route().get("/sitemap/*", sitemapController::sitemap);


        variables()
            .addGlobalVariable("page", new PageVariable(pageService))
            .addGlobalVariable("request", new RequestVariable())
            .addGlobalVariable("site", new SiteVariable(site()));


        if (site().isAdminEnabled()) {
            configureAdmin(pageService, pageIndexService, sitemapService);
        }
    }

    void configureAdmin(PageService pageService, PageIndexService pageIndexService, SitemapService sitemapService) {
        AdminPageRESTController adminPageRESTController = new AdminPageRESTController(pageService, pageIndexService, scheduler());
        admin().route()
            .get("/admin/api/site", request -> {
                Map<String, Object> site = Maps.newHashMap();
                site.put("host", site().host());
                return Response.bean(site);
            })
            .get("/admin/api/page/", adminPageRESTController::findPages)
            .post("/admin/api/page", adminPageRESTController::createPage)
            .get("/admin/api/page/:id", adminPageRESTController::getPage)
            .put("/admin/api/page/:id", adminPageRESTController::updatePage)
            .delete("/admin/api/page/:id", adminPageRESTController::deletePage)
            .get("/admin/api/page/rebuild-index", adminPageRESTController::rebuildIndex);

        AdminSitemapRESTController adminSitemapRESTController = new AdminSitemapRESTController(sitemapService, scheduler());
        admin().route().get("/admin/api/page/rebuild-sitemap", adminSitemapRESTController::rebuildSitemap);
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
