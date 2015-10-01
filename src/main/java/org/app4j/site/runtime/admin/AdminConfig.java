package org.app4j.site.runtime.admin;

import com.google.common.base.Preconditions;
import org.app4j.site.Module;
import org.app4j.site.runtime.InternalModule;
import org.app4j.site.runtime.route.RouteConfig;
import org.app4j.site.runtime.template.ClasspathResourceRepository;
import org.app4j.site.runtime.template.FolderResourceRepository;
import org.app4j.site.runtime.template.ResourceRepository;
import org.app4j.site.runtime.template.TemplateConfig;
import org.app4j.site.runtime.template.web.AssetsHandler;
import org.app4j.site.web.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author chi
 */
public class AdminConfig extends InternalModule {
    private final Logger logger = LoggerFactory.getLogger(AdminConfig.class);

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(RouteConfig.class, TemplateConfig.class);
    }

    public AdminConfig get(String path, Handler handler) {
        Preconditions.checkState(path.startsWith("/admin"), "admin route must start with /admin/");
        logger.info("route[admin] GET {}", path);
        route().get(path, new AdminHandler(site(), handler));
        return this;
    }

    public AdminConfig post(String path, Handler handler) {
        Preconditions.checkState(path.startsWith("/admin"), "admin route must start with /admin/");
        logger.info("route[admin] POST {}", path);
        route().post(path, new AdminHandler(site(), handler));
        return this;
    }

    public AdminConfig put(String path, Handler handler) {
        Preconditions.checkState(path.startsWith("/admin"), "admin route must start with /admin/");
        logger.info("route[admin] PUT {}", path);
        route().put(path, new AdminHandler(site(), handler));
        return this;
    }


    public AdminConfig delete(String path, Handler handler) {
        Preconditions.checkState(path.startsWith("/admin"), "admin route must start with /admin/");
        logger.info("route[admin] DELETE {}", path);
        route().delete(path, new AdminHandler(site(), handler));
        return this;
    }

    @Override
    protected void configure() throws Exception {
        ResourceRepository resourceRepository;

        if (property("site.admin.dir").isPresent()) {
            resourceRepository = new FolderResourceRepository(new File(property("site.admin.dir").get()));
        } else {
            resourceRepository = new ClasspathResourceRepository("admin/");
        }


        template().add(resourceRepository);
        template().assets().add(resourceRepository);

        bind(AdminConfig.class).to(this).export();

        get("/admin/index.html", new AdminHandler(site(), new AssetsHandler(template().assets())));
        get("/admin/assets/*", new AdminHandler(site(), new AssetsHandler(template().assets())));
    }

    @Override
    protected String name() {
        return "admin";
    }
}
