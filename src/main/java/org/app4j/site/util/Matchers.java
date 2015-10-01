package org.app4j.site.util;

import org.app4j.site.web.Parameter;

import java.util.regex.Pattern;

/**
 * @author chi
 */
public class Matchers {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-z0-9_-]{3,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})");

    public static Parameter.Matcher<String> id() {
        return value -> USERNAME_PATTERN.matcher(value).matches();
    }

    public static Parameter.Matcher<String> email() {
        return value -> EMAIL_PATTERN.matcher(value).matches();
    }

    public static Parameter.Matcher<String> password() {
        return value -> PASSWORD_PATTERN.matcher(value).matches();
    }
}
