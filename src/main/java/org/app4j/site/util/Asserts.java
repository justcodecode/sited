package org.app4j.site.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author chi
 */
public class Asserts {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-z0-9_-]{3,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})");

    private boolean failFast = false;
    private List<AssertError> errors = Lists.newArrayList();

    public static Asserts.Matcher<String> username() {
        return value -> !Strings.isNullOrEmpty(value) && USERNAME_PATTERN.matcher(value).matches();
    }

    public static Asserts.Matcher<String> email() {
        return value -> !Strings.isNullOrEmpty(value) && EMAIL_PATTERN.matcher(value).matches();
    }

    public static Asserts.Matcher<String> password() {
        return value -> !Strings.isNullOrEmpty(value) && PASSWORD_PATTERN.matcher(value).matches();
    }

    public <T> Assert<Asserts, T> assertThat(T value) {
        return new Assert<>(this, value);
    }

    public Asserts failFast() {
        failFast = true;
        return this;
    }

    public List<AssertError> errors() {
        return errors;
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }

    public void throwIfAny() {
        if (!errors.isEmpty()) {
            StringBuilder b = new StringBuilder();
            for (AssertError error : errors) {
                b.append(error.message).append('\n');
            }
            throw new IllegalArgumentException(b.toString());
        }
    }

    public interface Matcher<T> {
        boolean matches(T value);
    }

    public class Assert<H, T> {
        private final H holder;
        private final T value;

        public Assert(H holder, T value) {
            this.holder = holder;
            this.value = value;
        }

        public H is(Matcher<T> matcher) {
            if (matcher.matches(value)) {
                return holder;
            }
            return fail(String.format("value %s doesn't match %s", value, matcher));
        }

        protected H fail(String message) {
            if (failFast) {
                throw new IllegalArgumentException(message);
            } else {
                errors.add(new AssertError(value, message));
            }

            return holder;
        }

        public H notNull() {
            if (value == null) {
                fail("value can't be null");
            }
            return holder;
        }
    }

    public class AssertError {
        final Object value;
        final String message;

        public AssertError(Object value, String message) {
            this.value = value;
            this.message = message;
        }
    }
}
