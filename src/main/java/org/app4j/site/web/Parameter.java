package org.app4j.site.web;


import com.google.common.base.Preconditions;
import org.app4j.site.util.Value;

/**
 * @author chi
 */
public class Parameter<T> extends Value<T> {
    public Parameter(String name, T value) {
        super(name, value);
    }

    @Override
    public T get() {
        Preconditions.checkNotNull(value, "missing parameter %s", name);
        return value;
    }

    @Override
    public Parameter<T> assertThat(Matcher<T> matcher) {
        Preconditions.checkState(matcher.matches(value), "parameter %s's value %s doesn't match %s", name, value, matcher);
        return this;
    }
}
