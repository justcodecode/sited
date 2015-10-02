package org.app4j.site;

import com.google.common.base.Objects;

/**
 * @author chi
 */
public class Binding<T> {
    public final Key<T> key;
    public final Provider<T> provider;
    public final Class<? extends Scope> scope;

    public Binding(Key<T> key, Provider<T> provider, Class<? extends Scope> scope) {
        this.key = key;
        this.provider = provider;
        this.scope = scope;
    }

    public interface Export {
        void export();
    }

    public interface Provider<T> {
        T get(Key<T> key, Scope scope);
    }

    public interface To<T> {
        default Export to(final T instance) {
            return to((key1, context) -> instance);
        }

        Export to(Provider<T> provider);
    }

    public interface Named<T> extends To<T> {
        To<T> named(String name);
    }

    public static class Key<T> {
        public final Class<T> type;
        public final String qualifier;

        public Key(Class<T> type) {
            this(type, null);
        }

        public Key(Class<T> type, String qualifier) {
            this.type = type;
            this.qualifier = qualifier;
        }

        public Key<T> forAny() {
            return new Key<>(type, "*");
        }

        public Key<T> named(String qualifier) {
            return new Key<>(type, qualifier);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(type, qualifier);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Key other = (Key) obj;
            return Objects.equal(this.type, other.type) && Objects.equal(this.qualifier, other.qualifier);
        }

        @Override
        public String toString() {
            return type.getName() + '/' + (qualifier == null ? "" : qualifier);
        }
    }
}
