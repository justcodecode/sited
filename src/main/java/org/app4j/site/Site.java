package org.app4j.site;

import ch.qos.logback.classic.Level;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.mongodb.MongoClientURI;
import org.app4j.site.runtime.admin.AdminConfig;
import org.app4j.site.runtime.cache.CacheConfig;
import org.app4j.site.runtime.database.DatabaseConfig;
import org.app4j.site.runtime.error.ErrorConfig;
import org.app4j.site.runtime.event.EventConfig;
import org.app4j.site.runtime.i18n.I18nConfig;
import org.app4j.site.runtime.route.RouteConfig;
import org.app4j.site.runtime.template.Assets;
import org.app4j.site.runtime.template.TemplateConfig;
import org.app4j.site.util.Graph;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
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
    private final MongoClientURI mongoClientURI;
    private final Locale locale;
    private final Charset charset;

    @SuppressWarnings("unchecked")
    public Site(File dir, MongoClientURI mongoClientURI) {
        this.dir = dir;
        this.mongoClientURI = mongoClientURI;

        SiteLogger siteLogger = new SiteLogger();
        if (isDebugEnabled()) {
            siteLogger.setLevel(Level.DEBUG);
        }

        charset = Charset.forName(property("charset").orElse(Charsets.UTF_8.name()).get());
        locale = Locale.forLanguageTag(property("locale").orElse(Locale.getDefault().toLanguageTag()).get());


        install(new DatabaseConfig());
        install(new RouteConfig());
        install(new TemplateConfig());
        install(new EventConfig());
        install(new CacheConfig());
        install(new ErrorConfig());
        install(new I18nConfig());
        install(new AdminConfig());
        install(this);

        bind(Site.class).to(this);
    }

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(RouteConfig.class,
                TemplateConfig.class,
                EventConfig.class,
                CacheConfig.class,
                DatabaseConfig.class,
                AdminConfig.class,
                ErrorConfig.class);
    }


    public Site onShutdown(Runnable shutdownHook) {
        shutdownHooks.add(shutdownHook);
        return this;
    }

    public Site install(Module module) {
        logger.info("install module {}", module.getClass().getName());
        modules.put(module.getClass(), module);

        Queue<Class<? extends Module>> queue = new LinkedList<>();
        queue.addAll(module.dependencies());

        while (!queue.isEmpty()) {
            Class<? extends Module> dependencyModuleType = queue.poll();
            if (!modules.containsKey(dependencyModuleType)) {
                try {
                    Module dependencyModule = dependencyModuleType.getDeclaredConstructor().newInstance();
                    install(dependencyModule);
                    queue.addAll(dependencyModule.dependencies());
                } catch (Exception e) {
                    throw new Error(String.format("failed to install %s dependency %s", module.getClass(), dependencyModuleType), e);
                }
            }
        }
        return this;
    }

    public Site install(Class<? extends Module> moduleType) {
        return this;
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

    public Response handle(Request request) throws IOException {
//        Handler handler = route().find(request.method(), request.path(), request.parameters());
//        if (handler == null) {
//            throw new NotFoundException(request.path());
//        }
//        handler.handle(request, response);
        return null;
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

    @Override
    protected String name() {
        return "site";
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

    public Assets assets() {
        return require(Assets.class);
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
        return property("admin", Boolean.class).orElse(true).get();
    }

    public String baseURL() {
        return property("baseURL").get();
    }

    @SuppressWarnings("unchecked")
    public List<String> baseCdnURLs() {
        return property("cdnURLs", List.class).orElse(Lists.newArrayList(baseURL())).get();
    }
}
