package org.app4j.site;

import ch.qos.logback.classic.Level;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.mongodb.MongoClientURI;
import org.app4j.site.runtime.admin.AdminConfig;
import org.app4j.site.runtime.admin.AdminModule;
import org.app4j.site.runtime.cache.CacheConfig;
import org.app4j.site.runtime.database.DatabaseConfig;
import org.app4j.site.runtime.error.ErrorConfig;
import org.app4j.site.runtime.error.ErrorHandler;
import org.app4j.site.runtime.event.EventConfig;
import org.app4j.site.runtime.i18n.I18nConfig;
import org.app4j.site.runtime.route.RouteConfig;
import org.app4j.site.runtime.route.RouteModule;
import org.app4j.site.runtime.template.AssetsConfig;
import org.app4j.site.runtime.template.TemplateConfig;
import org.app4j.site.util.Graph;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

/**
 * @author chi
 */
public class Site extends Module {
    private final List<Runnable> shutdownHooks = Lists.newArrayList();
    private final Logger logger = LoggerFactory.getLogger(Site.class);
    private final Map<Class<? extends Module>, Module> modules = new HashMap<>();

    private final File dir;
    private final Locale locale;
    private final Charset charset;

    @SuppressWarnings("unchecked")
    public Site(File dir, MongoClientURI mongoClientURI) {
        this.dir = dir;
        SiteLogger siteLogger = new SiteLogger();

        if (isDebugEnabled()) {
            siteLogger.setLevel(Level.DEBUG);
        }

        charset = Charset.forName(property("charset").orElse(Charsets.UTF_8.name()).get());
        locale = Locale.forLanguageTag(property("locale").orElse(Locale.getDefault().toLanguageTag()).get());
        bind(Site.class).to(this);

        install(new DatabaseConfig(mongoClientURI));
        install(new RouteModule());
        install(new TemplateConfig());
        install(new EventConfig());
        install(new CacheConfig());
        install(new ErrorConfig());
        install(new I18nConfig());
        install(new AdminModule());
        install(this);
    }

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(RouteModule.class,
                TemplateConfig.class,
                EventConfig.class,
                CacheConfig.class,
                DatabaseConfig.class,
                AdminModule.class,
                ErrorConfig.class);
    }


    public Site onShutdown(Runnable shutdownHook) {
        shutdownHooks.add(shutdownHook);
        return this;
    }

    public Site install(Module module) {
        logger.info("install module [{}]", module.getClass().getCanonicalName());
        modules.put(module.getClass(), module);

        Queue<Class<? extends Module>> dependencies = new LinkedList<>();
        dependencies.addAll(module.dependencies());

        while (!dependencies.isEmpty()) {
            Class<? extends Module> dependentModuleType = dependencies.poll();
            if (!modules.containsKey(dependentModuleType)) {
                install(dependentModuleType);
            }
        }
        return this;
    }

    public Site install(Class<? extends Module> moduleType) {
        try {
            return install(moduleType.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public final void start() {
        for (final Class<? extends Module> moduleType : dependencyGraph()) {
            try {
                Module module = modules.get(moduleType);
                module.configure(this);
            } catch (Exception e) {
                logger.error("failed to start module {}", moduleType.getName(), e);
                throw new Error(e);
            }
        }
    }

    private Graph<Class<? extends Module>> dependencyGraph() {
        Graph<Class<? extends Module>> graph = new Graph<>();
        for (Map.Entry<Class<? extends Module>, Module> entry : modules.entrySet()) {
            List<Class<? extends Module>> dependencies = new ArrayList<>();
            dependencies.addAll(entry.getValue().dependencies());
            graph.add(entry.getKey(), dependencies);
        }
        return graph;
    }

    public Response handle(Request request) throws Exception {
        Handler handler = route().find(request.method(), request.path(), request.parameters());
        if (handler == null) {
            throw new NotFoundException(request.path());
        }
        try {
            return handler.handle(request);
        } catch (Exception e) {
            ErrorHandler errorHandler = error().handler(e.getClass());
            if (errorHandler != null) {
                return errorHandler.handle(request, e);
            }
            throw e;
        }
    }

    public final void stop() {
        Lists.reverse(shutdownHooks).forEach(java.lang.Runnable::run);
    }

    @Override
    protected void configure() throws Exception {
//        admin().get("/admin/api/template/", new TemplateRESTController(template())::all);
//
//        error().handle(NotFoundException.class, (request, response) -> response.setStatusCode(404));
//        error().handle(Exception.class, (request, response) -> response.setStatusCode(500));
    }

    public <T> T require(Class<T> type) {
        return super.require(type);
    }

    public <T> T require(Class<T> type, String qualifier) {
        return super.require(type, qualifier);
    }

    public TemplateConfig template() {
        return require(TemplateConfig.class);
    }

    public ErrorConfig error() {
        return require(ErrorConfig.class);
    }

    public CacheConfig cache() {
        return require(CacheConfig.class);
    }

    public EventConfig event() {
        return require(EventConfig.class);
    }

    public DatabaseConfig database() {
        return require(DatabaseConfig.class);
    }

    public RouteConfig route() {
        return require(RouteConfig.class);
    }

    public I18nConfig i18n() {
        return require(I18nConfig.class);
    }

    public AssetsConfig assets() {
        return require(AssetsConfig.class);
    }

    public AdminConfig admin() {
        return require(AdminConfig.class);
    }

    public Charset charset() {
        return charset;
    }

    public Locale locale() {
        return locale;
    }

    public File dir() {
        return dir;
    }

    public File dir(String name) {
        File dir = new File(this.dir, name);
        if (!dir.exists()) {
            try {
                Files.createParentDirs(dir);
            } catch (IOException e) {
                throw new Error(e);
            }
        }
        return dir;
    }

    public final boolean isDebugEnabled() {
        return property("debug", Boolean.class).orElse(true).get();
    }

    public boolean isAdminEnabled() {
        return property("sited/admin", Boolean.class).orElse(true).get();
    }

    public String baseURL() {
        return property("baseURL").get();
    }

    @SuppressWarnings("unchecked")
    public List<String> baseCdnURLs() {
        return property("cdnURLs", List.class).orElse(Lists.newArrayList(baseURL())).get();
    }
}
