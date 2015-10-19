package org.app4j.site;

import org.app4j.site.internal.admin.AdminConfig;
import org.app4j.site.internal.admin.AdminModule;
import org.app4j.site.internal.cache.CacheConfig;
import org.app4j.site.internal.cache.CacheModule;
import org.app4j.site.internal.database.DatabaseConfig;
import org.app4j.site.internal.database.DatabaseModule;
import org.app4j.site.internal.error.ErrorConfig;
import org.app4j.site.internal.error.ErrorModule;
import org.app4j.site.internal.index.IndexConfig;
import org.app4j.site.internal.route.RouteConfig;
import org.app4j.site.internal.route.RouteModule;
import org.app4j.site.internal.scheduler.SchedulerConfig;
import org.app4j.site.internal.scheduler.SchedulerModule;
import org.app4j.site.internal.template.TemplateConfig;
import org.app4j.site.internal.template.TemplateModule;
import org.app4j.site.internal.track.TrackConfig;

import java.util.Arrays;
import java.util.List;

/**
 * @author chi
 */
public abstract class Module extends ScopeImpl {
    private final Site site;

    public Module(Site site) {
        super(site);
        this.site = site;
    }

    protected abstract void configure() throws Exception;

    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(AdminModule.class,
            CacheModule.class, DatabaseModule.class,
            ErrorModule.class, SchedulerModule.class,
            SchedulerModule.class, RouteModule.class,
            TemplateModule.class);
    }

    protected Module onShutdown(Runnable shutdownHook) {
        site().onShutdown(new Site.Hook(this, shutdownHook));
        return this;
    }

    protected Module onStartup(Runnable startupHook) {
        site().onStartup(new Site.Hook(this, startupHook));
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

    protected SchedulerConfig scheduler() {
        return site().scheduler();
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
