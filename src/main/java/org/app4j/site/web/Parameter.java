package org.app4j.site.web;


import com.google.common.base.Preconditions;
import org.app4j.site.util.Asserts;
import org.app4j.site.util.Value;
import org.app4j.site.web.exception.BadRequestException;

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

    public Parameter<T> assertThat(Asserts.Matcher<T> matcher) {
        if (!matcher.matches(value)) {
            throw new BadRequestException(name, value, "value not match " + matcher.toString());
        }
        return this;
    }
}
