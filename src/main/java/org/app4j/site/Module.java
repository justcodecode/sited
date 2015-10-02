package org.app4j.site;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.app4j.site.runtime.admin.AdminConfig;
import org.app4j.site.runtime.cache.CacheConfig;
import org.app4j.site.runtime.database.DatabaseConfig;
import org.app4j.site.runtime.error.ErrorConfig;
import org.app4j.site.runtime.event.EventConfig;
import org.app4j.site.runtime.i18n.I18nConfig;
import org.app4j.site.runtime.route.RouteConfig;
import org.app4j.site.runtime.template.TemplateConfig;
import org.app4j.site.util.JSON;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public abstract class Module implements Scope, Iterable<Binding<?>> {
    private final Map<Binding.Key<?>, Binding<?>> bindings = new HashMap<>();
    private Module parent;

    final void configure(Module parent) throws Exception {
        this.parent = parent;
        configure();
    }

    protected abstract void configure() throws Exception;

    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(Site.class);
    }

    protected <T> Binding.Named<T> bind(final Class<T> type) {
        return new Binding.Named<T>() {
            Binding.Key<T> key = new Binding.Key<>(type);

            @Override
            public Binding.To<T> named(String qualifier) {
                Preconditions.checkNotNull(qualifier, "qualifier can't be null");
                this.key = new Binding.Key<T>(type, qualifier);
                return this;
            }

            @Override
            public Binding.Export to(final Binding.Provider<T> supplier) {
                final Binding<T> binding = new Binding<>(key, supplier, Module.this.getClass());
                put(binding);
                return () -> parent.put(binding);
            }
        };
    }

    final <T> void put(Binding<T> binding) {
        bindings.put(binding.key, binding);
    }

    public <T> T require(Class<T> type) {
        return require(type, null);
    }

    public <T> T require(Class<T> type, String qualifier) {
        Binding.Key<T> key = new Binding.Key<T>(type, qualifier);
        Binding<T> binding = get(key);
        return binding.provider.get(key, this);
    }

    @SuppressWarnings("unchecked")
    private <T> Binding<T> get(Binding.Key<T> key) {
        Binding<T> binding = (Binding<T>) this.bindings.get(key);
        if (binding == null && parent != null && !this.equals(parent)) {
            binding = parent.get(key);
        }
        Preconditions.checkNotNull(binding, "missing binding %s", key);
        return binding;
    }

    @Override
    public Iterator<Binding<?>> iterator() {
        return Lists.newArrayList(bindings.values()).iterator();
    }

    protected Module onShutdown(Runnable shutdownHook) {
        require(Site.class).onShutdown(shutdownHook);
        return this;
    }

    protected Module onStartup(Runnable startupHook) {
        return this;
    }

    protected RouteConfig route() {
        return require(RouteConfig.class);
    }

    protected Site site() {
        return (Site) parent;
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

    protected I18nConfig i18n() {
        return site().i18n();
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
