package org.app4j.site.util;

import com.google.common.base.Preconditions;

/**
 * @author chi
 */
public class Value<T> {
    public final String name;
    public final T value;

    public Value(String name, T value) {
        Preconditions.checkNotNull(name, "name can't be null");
        this.name = name;
        this.value = value;
    }

    public Value<T> orElse(T value) {
        return this.value == null ? new Value<>(name, value) : this;
    }

    public boolean isPresent() {
        return value != null;
    }

    public T get() {
        Preconditions.checkNotNull(value, "value can't be null");
        return value;
    }

    public Value<T> assertThat(Matcher<T> matcher) {
        Preconditions.checkState(matcher.matches(value), "%s's value %s doesn't match %s", name, value, matcher);
        return this;
    }

    public interface Matcher<T> {
        boolean matches(T value);
    }
}
