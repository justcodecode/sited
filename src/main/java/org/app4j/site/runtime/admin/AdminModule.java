package org.app4j.site.runtime.admin;

import com.google.common.base.Preconditions;
import org.app4j.site.Module;
import org.app4j.site.runtime.InternalModule;
import org.app4j.site.runtime.admin.codec.ProfileCodec;
import org.app4j.site.runtime.admin.service.AdminTemplateRepository;
import org.app4j.site.runtime.admin.service.Console;
import org.app4j.site.runtime.admin.service.ProfileService;
import org.app4j.site.runtime.admin.web.AdminController;
import org.app4j.site.runtime.admin.web.AdminHandler;
import org.app4j.site.runtime.route.RouteConfig;
import org.app4j.site.runtime.route.RouteModule;
import org.app4j.site.runtime.template.TemplateModule;
import org.app4j.site.runtime.template.web.AssetsHandler;
import org.app4j.site.util.ClasspathResourceRepository;
import org.app4j.site.util.FolderResourceRepository;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class AdminModule extends InternalModule {
    private final Console console = new Console();

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(RouteModule.class, TemplateModule.class);
    }

    @Override
    protected void configure() throws Exception {
        database().codecs().add(new ProfileCodec());

        ProfileService profileService = new ProfileService(database().get());
        bind(ProfileService.class).to(profileService);

        AdminConfig adminConfig = new AdminConfigImpl();
        bind(AdminConfig.class).to(adminConfig);

        AdminTemplateRepository resourceRepository;

        if (property("site.admin.dir").isPresent()) {
            resourceRepository = new AdminTemplateRepository(new FolderResourceRepository(new File(property("site.admin.dir").get())));
        } else {
            resourceRepository = new AdminTemplateRepository(new ClasspathResourceRepository("sited/"));
        }

        template().add(resourceRepository);


        AssetsHandler assetsHandler = new AssetsHandler(template().assets()).enableCache().cacheExpireAfter(120);
        route().get("/admin/assets/lib/*", assetsHandler)
                .get("/admin/login.html", assetsHandler);

        AdminController adminController = new AdminController(site(), profileService, template().assets());
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
                    AdminModule.this.route().get(route, new AdminHandler(site(), handler));
                    return this;
                }

                @Override
                public RouteConfig post(String route, Handler handler) {
                    Preconditions.checkState(route.startsWith("/admin/"), "admin route must start with /admin/");
                    AdminModule.this.route().post(route, new AdminHandler(site(), handler));
                    return this;
                }

                @Override
                public RouteConfig put(String route, Handler handler) {
                    Preconditions.checkState(route.startsWith("/admin/"), "admin route must start with /admin/");
                    AdminModule.this.route().put(route, new AdminHandler(site(), handler));
                    return this;
                }

                @Override
                public RouteConfig delete(String route, Handler handler) {
                    Preconditions.checkState(route.startsWith("/admin/"), "admin route must start with /admin/");
                    AdminModule.this.route().delete(route, new AdminHandler(site(), handler));
                    return this;
                }

                @Override
                public Handler find(Request.Method method, String path, Map<String, String> parameters) {
                    throw new Error("not implemented");
                }
            };
        }
    }
}
