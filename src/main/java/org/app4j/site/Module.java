package org.app4j.site;

import org.app4j.site.runtime.admin.AdminConfig;
import org.app4j.site.runtime.cache.CacheConfig;
import org.app4j.site.runtime.database.DatabaseConfig;
import org.app4j.site.runtime.error.ErrorModule;
import org.app4j.site.runtime.event.EventModule;
import org.app4j.site.runtime.route.RouteConfig;
import org.app4j.site.runtime.template.TemplateConfig;
import org.app4j.site.util.JSON;

import java.util.Arrays;
import java.util.List;

/**
 * @author chi
 */
public abstract class Module extends DefaultScope {
    public Module() {
        super(null);
    }

    protected abstract void configure() throws Exception;

    final void configure(Module parent) throws Exception {
        this.parent = parent;
        configure();
    }


    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(Site.class);
    }

    protected Module onShutdown(Runnable shutdownHook) {
        site().onShutdown(shutdownHook);
        return this;
    }

    protected Module onStartup(Runnable startupHook) {
        return this;
    }

    protected RouteConfig route() {
        return require(RouteConfig.class);
    }

    protected Site site() {
        return require(Site.class);
    }

    protected DatabaseConfig database() {
        return site().database();
    }

    protected EventModule event() {
        return site().event();
    }

    protected TemplateConfig template() {
        return site().template();
    }

    protected CacheConfig cache() {
        return site().cache();
    }

    protected ErrorModule error() {
        return site().error();
    }

    protected AdminConfig admin() {
        return site().admin();
    }

    protected <T> Property<T> property(String key, Class<T> type) {
        String value = System.getProperty(key);
        if (value == null) {
            return new Property<>(key, null);
        }
        return new Property<>(key, JSON.mapper().convertValue(value, type));
    }

    protected Property<String> property(String key) {
        return property(key, String.class);
    }
}
