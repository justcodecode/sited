package org.app4j.site.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.app4j.site.web.exception.BadRequestException;

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
    private final List<AssertionErrorMessage> assertionErrorMessages = Lists.newArrayList();
    private boolean failFast = false;

    public static Asserts.Matcher<String> username() {
        return value -> !Strings.isNullOrEmpty(value) && USERNAME_PATTERN.matcher(value).matches();
    }

    public static Asserts.Matcher<String> email() {
        return value -> !Strings.isNullOrEmpty(value) && EMAIL_PATTERN.matcher(value).matches();
    }

    public static Asserts.Matcher<String> password() {
        return value -> !Strings.isNullOrEmpty(value) && PASSWORD_PATTERN.matcher(value).matches();
    }

    public <T> Assert<Asserts, T> assertThat(Value<T> value) {
        return new Assert<>(this, value);
    }

    public Asserts failFast() {
        failFast = true;
        return this;
    }

    public List<AssertionErrorMessage> errors() {
        return assertionErrorMessages;
    }

    public void throwIfAny() {
        if (!assertionErrorMessages.isEmpty()) {
            throw new BadRequestException(assertionErrorMessages);
        }
    }

    public interface Matcher<T> {
        boolean matches(T value);
    }

    public static class AssertionErrorMessage {
        private final String name;
        private final Object value;
        private final String message;

        public AssertionErrorMessage(String name, Object value, String message) {
            this.name = name;
            this.value = value;
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }

        public String getMessage() {
            return message;
        }
    }

    public class Assert<H, T> {
        private final H holder;
        private final Value<T> value;

        public Assert(H holder, Value<T> value) {
            this.holder = holder;
            this.value = value;
        }

        public H is(Matcher<T> matcher) {
            if (matcher.matches(value.value)) {
                return holder;
            }
            return fail(String.format("value %s doesn't match %s", value, matcher));
        }

        protected H fail(String message) {
            if (failFast) {
                throw new IllegalArgumentException(message);
            } else {
                assertionErrorMessages.add(new AssertionErrorMessage(value.name, value.value, message));
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
}
