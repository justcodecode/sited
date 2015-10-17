package org.app4j.site;

import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.Properties;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class Site extends DefaultScope {
    private final List<Hook> shutdownHooks = Lists.newArrayList();

    private final Logger logger = LoggerFactory.getLogger(Site.class);
    private final Map<Class<? extends Module>, Module> modules = new HashMap<>();
    private final Properties properties;

    private final File dir;
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

        install(new DatabaseModule());
        install(new RouteModule());
        install(new TemplateModule(dir("web")));
        install(new EventModule());
        install(new CacheModule(dir("cache")));
        install(new ErrorModule());
        install(new AdminModule());
        install(new IndexModule());

        bind(Site.class).to(this);
    }


    private Properties loadProperties() {
        Properties properties = new Properties();
        File file = new File(dir, "site.properties");
        Preconditions.checkState(file.exists(), "missing config file, %s", file.getAbsolutePath());
        try (InputStream inputStream = new FileInputStream(file)) {
            properties.load(new InputStreamReader(inputStream, Charsets.UTF_8));
        } catch (IOException e) {
            throw new Error(e);
        }
        return properties;
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

    public Site onShutdown(Hook shutdownHook) {
        shutdownHooks.add(shutdownHook);
        return this;
    }

    public Site install(Module module) {
        if (modules.containsKey(module.getClass())) {
            return this;
        }
        logger.info("install module [%s]", module.getClass().getName());
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

    public final void stop() {
        Lists.reverse(shutdownHooks).forEach(hook -> {
            logger.info("run shutdown hook, %s -> %s", hook.module.getClass(), hook.runnable);
            hook.runnable.run();
        });
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
            context.setVariable("template", template.get());
            context.setVariables(model);
            return Response.text(template().engine().process(templatePath, context), "text/html");
        } catch (Throwable e) {
            ErrorHandler errorHandler = error().handler(e.getClass());
            return errorHandler.handle((Request) model.get("request"), e);
        }
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

    public EventConfig event() {
        return require(EventConfig.class);
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
