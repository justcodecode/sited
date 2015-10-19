package org.app4j.site;

import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.app4j.site.internal.admin.AdminConfig;
import org.app4j.site.internal.admin.AdminModule;
import org.app4j.site.internal.cache.CacheConfig;
import org.app4j.site.internal.cache.CacheModule;
import org.app4j.site.internal.database.DatabaseConfig;
import org.app4j.site.internal.database.DatabaseModule;
import org.app4j.site.internal.error.ErrorConfig;
import org.app4j.site.internal.error.ErrorHandler;
import org.app4j.site.internal.error.ErrorModule;
import org.app4j.site.internal.index.IndexConfig;
import org.app4j.site.internal.index.IndexModule;
import org.app4j.site.internal.route.RouteConfig;
import org.app4j.site.internal.route.RouteModule;
import org.app4j.site.internal.scheduler.SchedulerConfig;
import org.app4j.site.internal.scheduler.SchedulerModule;
import org.app4j.site.internal.template.TemplateConfig;
import org.app4j.site.internal.template.TemplateModule;
import org.app4j.site.internal.track.TrackConfig;
import org.app4j.site.util.Graph;
import org.app4j.site.util.JSON;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class Site extends ScopeImpl {
    private final List<Hook> shutdownHooks = Lists.newArrayList();
    private final List<Hook> startupHooks = Lists.newArrayList();

    private final Logger logger = LoggerFactory.getLogger(Site.class);
    private final Map<Class<? extends Module>, Module> modules = new HashMap<>();
    private final Properties properties;

    private final File dir;
    private final String name;
    private final String description;
    private final String logoURL;
    private final Locale locale;
    private final Charset charset;
    private final Boolean adminEnabled;
    private final Boolean debugEnabled;
    private final String baseURL;
    private final List<String> baseCdnURLs;
    private final String host;
    private final Integer port;

    public Site(File dir) {
        super(null);
        this.dir = dir;
        this.properties = loadProperties();

        host = property("site.host").orElse("0.0.0.0").get();
        port = property("site.port", Integer.class).orElse(8080).get();

        name = property("site.name").orElse(host()).get();
        description = property("site.description").orElse("").get();
        logoURL = property("site.logoURL").orElse("").get();

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

        install(DatabaseModule.class);
        install(RouteModule.class);
        install(TemplateModule.class);
        install(SchedulerModule.class);
        install(CacheModule.class);
        install(ErrorModule.class);
        install(AdminModule.class);
        install(IndexModule.class);

        bind(Site.class).to(this);
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        File file = new File(dir, "site.properties");
        Preconditions.checkState(file.exists(), "missing config file, %s", file.getAbsolutePath());

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), Charsets.UTF_8)) {
            properties.load(reader);
            return properties;
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public String logoURL() {
        return logoURL;
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

    public Site onShutdown(Hook shutdownHook) {
        shutdownHooks.add(shutdownHook);
        return this;
    }

    public Site onStartup(Hook startupHook) {
        startupHooks.add(startupHook);
        return this;
    }

    public Site install(Class<? extends Module> moduleClass) {
        if (modules.containsKey(moduleClass)) {
            return this;
        }
        logger.info("install module [%s]", moduleClass.getName());

        try {
            Module module = moduleClass.getDeclaredConstructor(Site.class).newInstance(this);
            modules.put(moduleClass, module);

            Queue<Class<? extends Module>> dependencies = new LinkedList<>();
            dependencies.addAll(module.dependencies());

            while (!dependencies.isEmpty()) {
                Class<? extends Module> dependency = dependencies.poll();
                if (!modules.containsKey(dependency)) {
                    install(dependency);
                }
            }
            return this;
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            throw new Error(e);
        }
    }

    public final void start() {
        for (final Class<? extends Module> module : moduleGraph()) {
            try {
                modules.get(module).configure();
            } catch (Exception e) {
                logger.error("failed to start module %s", module.getName(), e);
                throw new Error(e);
            }
        }

        startupHooks.forEach(hook -> {
            logger.info("run startup hook, %s -> %s", hook.module.getClass(), hook.runnable);
            hook.runnable.run();
        });
    }

    public final void stop() {
        Lists.reverse(shutdownHooks).forEach(hook -> {
            logger.info("run shutdown hook, %s -> %s", hook.module.getClass(), hook.runnable);
            hook.runnable.run();
        });
    }

    private Graph<Class<? extends Module>> moduleGraph() {
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
            return handleError(request, e);
        }
    }

    public Response handleError(Request request, Throwable e) {
        ErrorHandler errorHandler = error().handler(e.getClass());
        return errorHandler.handle(request, e);
    }

    public <T> Property<T> property(String key, Class<T> type) {
        String value = properties.getProperty(key);
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
        return require(IndexConfig.class);
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

    public SchedulerConfig scheduler() {
        return require(SchedulerConfig.class);
    }

    public DatabaseConfig database() {
        return require(DatabaseConfig.class);
    }

    public RouteConfig route() {
        return require(RouteConfig.class);
    }

    public AdminConfig admin() {
        return require(AdminConfig.class);
    }

    public TrackConfig track() {
        return require(TrackConfig.class);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("dir", dir)
            .add("locale", locale)
            .add("charset", charset)
            .add("adminEnabled", adminEnabled)
            .add("debugEnabled", debugEnabled)
            .add("baseURL", baseURL)
            .add("baseCdnURLs", baseCdnURLs)
            .add("host", host)
            .add("port", port)
            .toString();
    }

    static class Hook {
        public final Module module;
        public final Runnable runnable;

        Hook(Module module, Runnable runnable) {
            this.module = module;
            this.runnable = runnable;
        }
    }
}
