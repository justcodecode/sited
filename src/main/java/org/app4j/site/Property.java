package org.app4j.site;

import com.google.common.base.Preconditions;
import org.app4j.site.util.Value;

/**
 * @author chi
 */
public class Property<T> extends Value<T> {
    public Property(String key, T value) {
        super(key, value);
    }

    public T get() {
        Preconditions.checkNotNull(value, "missing property %s", name);
        return value;
    }

    public Property<T> assertThat(Matcher<T> matcher) {
        Preconditions.checkState(matcher.matches(value), "property %s's value %s doesn't match %s", name, value, matcher);
        return this;
    }
}
