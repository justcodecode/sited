package org.app4j.site.runtime.admin;

import com.google.common.base.Preconditions;
import org.app4j.site.Module;
import org.app4j.site.runtime.InternalModule;
import org.app4j.site.runtime.admin.service.AdminResourceRepository;
import org.app4j.site.runtime.admin.service.Console;
import org.app4j.site.runtime.admin.web.AdminHandler;
import org.app4j.site.runtime.route.RouteConfig;
import org.app4j.site.runtime.route.RouteModule;
import org.app4j.site.runtime.template.ClasspathResourceRepository;
import org.app4j.site.runtime.template.FolderResourceRepository;
import org.app4j.site.runtime.template.ResourceRepository;
import org.app4j.site.runtime.template.TemplateModule;
import org.app4j.site.runtime.template.web.AssetsHandler;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.UnauthorizedException;

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
        AdminConfig adminConfig = new AdminConfigImpl();
        bind(AdminConfig.class).to(adminConfig).export();

        ResourceRepository resourceRepository;

        if (property("site.admin.dir").isPresent()) {
            resourceRepository = new AdminResourceRepository(new FolderResourceRepository(new File(property("site.admin.dir").get()), 0));
        } else {
            resourceRepository = new AdminResourceRepository(new ClasspathResourceRepository("sited/", 0));
        }


        template().add(resourceRepository);
        template().assets().add(resourceRepository);

        bind(AdminModule.class).to(this).export();

        AssetsHandler assetsHandler = new AssetsHandler(template().assets());
        route().get("/admin/login.html", assetsHandler);

        adminConfig.route()
                .get("/admin/api/template/", request -> Response.bean(template().all()))
                .get("/admin/index.html", assetsHandler)
                .get("/admin/assets/*", assetsHandler);

        error().on(UnauthorizedException.class, (request, e) -> Response.redirect("/admin/login.html"));
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
