package org.app4j.site;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author chi
 */
public class DefaultScope implements Scope, Iterable<Binding<?>> {
    private final Map<Binding.Key<?>, Binding<?>> bindings = new HashMap<>();
    protected DefaultScope parent;

    public DefaultScope(DefaultScope parent) {
        this.parent = parent;
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
                final Binding<T> binding = new Binding<>(key, supplier, DefaultScope.this.getClass());
                add(binding);
                return () -> parent.add(binding);
            }
        };
    }

    @Override
    public <T> T require(Class<T> type) {
        return require(type, null);
    }

    @Override
    public <T> T require(Class<T> type, String qualifier) {
        Binding.Key<T> key = new Binding.Key<T>(type, qualifier);
        Binding<T> binding = get(key);
        return binding.provider.get(key, this);
    }

    protected <T> void add(Binding<T> binding) {
        bindings.put(binding.key, binding);
    }

    @SuppressWarnings("unchecked")
    protected <T> Binding<T> get(Binding.Key<T> key) {
        Binding<T> binding = (Binding<T>) this.bindings.get(key);
        if (binding == null && parent != null) {
            binding = parent.get(key);
        }
        Preconditions.checkNotNull(binding, "missing binding %s", key);
        return binding;
    }

    @Override
    public Iterator<Binding<?>> iterator() {
        return Lists.newArrayList(bindings.values()).iterator();
    }
}
