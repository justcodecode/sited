package org.app4j.site.internal.admin;

import com.google.common.base.Preconditions;
import org.app4j.site.Site;
import org.app4j.site.internal.InternalModule;
import org.app4j.site.internal.admin.service.Console;
import org.app4j.site.internal.admin.service.InstallService;
import org.app4j.site.internal.admin.web.AdminController;
import org.app4j.site.internal.admin.web.AdminHandler;
import org.app4j.site.internal.database.DatabaseModule;
import org.app4j.site.internal.route.RouteConfig;
import org.app4j.site.internal.route.RouteModule;
import org.app4j.site.internal.route.Router;
import org.app4j.site.internal.template.AssetsConfig;
import org.app4j.site.internal.template.web.AssetsHandler;
import org.app4j.site.util.ClasspathResourceRepository;
import org.app4j.site.util.FolderResourceRepository;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.File;
import java.util.Optional;

/**
 * @author chi
 */
public class AdminModule extends InternalModule {
    private final Console console = new Console();
    private final AssetsConfig assetsConfig = new AssetsConfig();

    public AdminModule(Site site) {
        super(site);
        dependencies.add(RouteModule.class);
        dependencies.add(DatabaseModule.class);
    }

    @Override
    protected void configure() throws Exception {
        AdminConfig adminConfig = new AdminConfigImpl();
        bind(AdminConfig.class).to(adminConfig).export();

        InstallService installService = new InstallService(database().get());
        bind(InstallService.class).to(installService);


        if (property("site.admin.dir").isPresent()) {
            assetsConfig.add(new FolderResourceRepository(new File(property("site.admin.dir").get())));
        } else {
            assetsConfig.add(new ClasspathResourceRepository("/web"));
        }

        AssetsHandler assetsHandler = new AssetsHandler(assetsConfig).enableCache().cacheExpireAfter(120);
        route().get("/admin/assets/lib/*", assetsHandler)
            .get("/admin/login.html", assetsHandler);

        AdminController adminController = new AdminController(site, installService, assetsConfig);
        route().get("/admin/install.html", adminController::install)
            .post("/admin/profile", adminController::profile)
            .get("/admin/index.html", adminController::index);

        adminConfig.route()
            .get("/admin/assets/*", assetsHandler);

        adminConfig.route()
            .get("/admin/api/template/", request -> Response.bean(template().all()));
    }

    private class AdminConfigImpl implements AdminConfig {
        @Override
        public Console console() {
            return console;
        }

        @Override
        public RouteConfig route() {
            return new RouteConfig() {
                @Override
                public RouteConfig get(String route, Handler handler) {
                    Preconditions.checkState(route.startsWith("/admin/"), "admin route must start with /admin/");
                    AdminModule.this.route().get(route, new AdminHandler(site, handler));
                    return this;
                }

                @Override
                public RouteConfig post(String route, Handler handler) {
                    Preconditions.checkState(route.startsWith("/admin/"), "admin route must start with /admin/");
                    AdminModule.this.route().post(route, new AdminHandler(site, handler));
                    return this;
                }

                @Override
                public RouteConfig put(String route, Handler handler) {
                    Preconditions.checkState(route.startsWith("/admin/"), "admin route must start with /admin/");
                    AdminModule.this.route().put(route, new AdminHandler(site, handler));
                    return this;
                }

                @Override
                public RouteConfig delete(String route, Handler handler) {
                    Preconditions.checkState(route.startsWith("/admin/"), "admin route must start with /admin/");
                    AdminModule.this.route().delete(route, new AdminHandler(site, handler));
                    return this;
                }

                @Override
                public Optional<Router.Route> find(Request.Method method, String path) {
                    throw new Error("not implement");
                }
            };
        }
    }
}
