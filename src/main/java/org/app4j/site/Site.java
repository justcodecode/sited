package org.app4j.site;

import ch.qos.logback.classic.Level;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.mongodb.MongoClientURI;
import org.app4j.site.runtime.admin.AdminConfig;
import org.app4j.site.runtime.admin.AdminModule;
import org.app4j.site.runtime.cache.CacheConfig;
import org.app4j.site.runtime.cache.CacheModule;
import org.app4j.site.runtime.database.DatabaseConfig;
import org.app4j.site.runtime.database.DatabaseModule;
import org.app4j.site.runtime.error.ErrorConfig;
import org.app4j.site.runtime.error.ErrorHandler;
import org.app4j.site.runtime.error.ErrorModule;
import org.app4j.site.runtime.event.EventConfig;
import org.app4j.site.runtime.event.EventModule;
import org.app4j.site.runtime.index.IndexConfig;
import org.app4j.site.runtime.index.IndexModule;
import org.app4j.site.runtime.route.RouteConfig;
import org.app4j.site.runtime.route.RouteModule;
import org.app4j.site.runtime.template.TemplateConfig;
import org.app4j.site.runtime.template.TemplateModule;
import org.app4j.site.runtime.template.service.Template;
import org.app4j.site.util.Graph;
import org.app4j.site.util.JSON;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class Site extends DefaultScope {
    private final List<Runnable> shutdownHooks = Lists.newArrayList();
    private final Logger logger = LoggerFactory.getLogger(Site.class);
    private final Map<Class<? extends Module>, Module> modules = new HashMap<>();

    private final File dir;
    private final Locale locale;
    private final Charset charset;
    private final Boolean adminEnabled;
    private final Boolean debugEnabled;
    private final String baseURL;
    private final List<String> baseCdnURLs;
    private final String host;
    private final Integer port;
    private final DatabaseModule databaseModule;
    private final RouteModule routeModule;
    private final TemplateModule templateModule;
    private final EventModule eventModule;
    private final ErrorModule errorModule;
    private final AdminModule adminModule;
    private final CacheModule cacheModule;
    private final IndexModule indexModule;

    public Site(MongoClientURI mongoClientURI) {
        super(null);
        SiteLogger siteLogger = new SiteLogger();

        host = property("site.host").orElse("0.0.0.0").get();
        port = property("site.port", Integer.class).orElse(8080).get();
        this.dir = new File(property("site.dir").orElse(defaultDir().toString()).get());
        charset = Charset.forName(property("site.charset").orElse(Charsets.UTF_8.name()).get());
        locale = Locale.forLanguageTag(property("site.locale").orElse(Locale.getDefault().toLanguageTag()).get());
        debugEnabled = property("site.debug", Boolean.class).orElse(false).get();
        adminEnabled = property("site.admin", Boolean.class).orElse(true).get();
        baseURL = property("site.baseURL").orElse(defaultBaseURL()).get();
        if (property("site.baseCdnURLs").isPresent()) {
            baseCdnURLs = Arrays.stream(property("site.baseCdnURLs").get().split(","))
                    .filter(s -> !Strings.isNullOrEmpty(s))
                    .map(String::trim)
                    .collect(Collectors.toList());
        } else {
            baseCdnURLs = Lists.newArrayList(baseURL());
        }

        if (isDebugEnabled()) {
            siteLogger.setLevel(Level.DEBUG);
        }
        logger.info("use dir {}", dir);

        databaseModule = new DatabaseModule(mongoClientURI);
        install(databaseModule);
        routeModule = new RouteModule();
        install(routeModule);
        templateModule = new TemplateModule(dir("template"));
        install(templateModule);
        eventModule = new EventModule();
        install(eventModule);
        cacheModule = new CacheModule(dir("cache"));
        install(cacheModule);
        errorModule = new ErrorModule();
        install(errorModule);
        adminModule = new AdminModule();
        install(adminModule);
        indexModule = new IndexModule();
        install(indexModule);

        bind(Site.class).to(this);
    }

    private Path defaultDir() {
        return new File(property("user.home").get(), host()).toPath();
    }

    private String defaultBaseURL() {
        StringBuilder b = new StringBuilder();
        b.append("http://");
        b.append(host());
        if (port() != 80 && port() != 443) {
            b.append(':').append(port());
        }
        return b.toString();
    }


    public final String host() {
        return host;
    }

    public final int port() {
        return port;
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
        } catch (Throwable e) {
            logger.error("failed to handle ", e);
            ErrorHandler errorHandler = error().handler(e.getClass());
            return errorHandler.handle(request, e);
        }
    }

    public Response render(String templatePath, Map<String, Object> model) {
        try {
            Optional<Template> template = template().get(templatePath);
            if (!template.isPresent()) {
                throw new NotFoundException(templatePath);
            }
            Context context = new Context();
            context.setVariable("template", template);
            context.setVariables(model);
            return Response.text(template().engine().process(templatePath, context), "text/html");
        } catch (Throwable e) {
            ErrorHandler errorHandler = error().handler(e.getClass());
            return errorHandler.handle((Request) model.get("request"), e);
        }
    }

    public final void stop() {
        Lists.reverse(shutdownHooks).forEach(java.lang.Runnable::run);
    }

    public TemplateConfig template() {
        return templateModule;
    }

    public ErrorConfig error() {
        return errorModule;
    }

    public CacheConfig cache() {
        return cacheModule;
    }

    public EventConfig event() {
        return eventModule;
    }

    public DatabaseConfig database() {
        return databaseModule;
    }

    public RouteConfig route() {
        return routeModule;
    }

    public AdminConfig admin() {
        return adminModule.require(AdminConfig.class);
    }

    public <T> Property<T> property(String key, Class<T> type) {
        String value = System.getProperty(key);
        if (value == null) {
            return new Property<>(key, null);
        }
        return new Property<>(key, JSON.mapper().convertValue(value, type));
    }

    public Property<String> property(String key) {
        return property(key, String.class);
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
        return debugEnabled;
    }

    public boolean isAdminEnabled() {
        return adminEnabled;
    }

    public final String baseURL() {
        return baseURL;
    }

    public final List<String> baseCdnURLs() {
        return baseCdnURLs;
    }

    public IndexConfig index() {
        return indexModule;
    }
}
