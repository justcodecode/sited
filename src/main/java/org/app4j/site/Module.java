package org.app4j.site;

import org.app4j.site.runtime.admin.AdminConfig;
import org.app4j.site.runtime.admin.AdminModule;
import org.app4j.site.runtime.cache.CacheConfig;
import org.app4j.site.runtime.cache.CacheModule;
import org.app4j.site.runtime.database.DatabaseConfig;
import org.app4j.site.runtime.database.DatabaseModule;
import org.app4j.site.runtime.error.ErrorConfig;
import org.app4j.site.runtime.error.ErrorModule;
import org.app4j.site.runtime.event.EventConfig;
import org.app4j.site.runtime.event.EventModule;
import org.app4j.site.runtime.index.IndexConfig;
import org.app4j.site.runtime.route.RouteConfig;
import org.app4j.site.runtime.route.RouteModule;
import org.app4j.site.runtime.template.TemplateConfig;
import org.app4j.site.runtime.template.TemplateModule;
import org.app4j.site.runtime.track.TrackConfig;
import org.app4j.site.runtime.variable.VariableModule;

import java.util.Arrays;
import java.util.List;

/**
 * @author chi
 */
public abstract class Module extends DefaultScope {
    private final Site site;

    public Module(Site site) {
        super(site);
        this.site = site;
    }

    protected abstract void configure() throws Exception;

    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(AdminModule.class,
            CacheModule.class, DatabaseModule.class,
            ErrorModule.class, EventModule.class,
            EventModule.class, RouteModule.class,
            TemplateModule.class, VariableModule.class);
    }

    protected Module onShutdown(Runnable shutdownHook) {
        site().onShutdown(new Site.Hook(this, shutdownHook));
        return this;
    }

    protected Module onStartup(Runnable startupHook) {
        return this;
    }

    protected RouteConfig route() {
        return site().route();
    }

    protected Site site() {
        return site;
    }

    protected DatabaseConfig database() {
        return site().database();
    }

    protected EventConfig event() {
        return site().event();
    }

    protected TemplateConfig template() {
        return site().template();
    }

    protected CacheConfig cache() {
        return site().cache();
    }

    protected ErrorConfig error() {
        return site().error();
    }

    protected AdminConfig admin() {
        return site().admin();
    }

    protected IndexConfig index() {
        return site().index();
    }

    protected TrackConfig track() {
        return site().track();
    }

    protected <T> Property<T> property(String key, Class<T> type) {
        return site().property(key, type);
    }

    protected Property<String> property(String key) {
        return property(key, String.class);
    }
}
