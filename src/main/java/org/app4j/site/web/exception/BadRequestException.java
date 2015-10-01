package org.app4j.site.web.exception;

/**
 * @author chi
 */
public class BadRequestException extends RuntimeException {
    private final String name;
    private final Object value;

    public BadRequestException(String name, Object value, String message) {
        super(String.format(message, name, value));
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public Object value() {
        return value;
    }
}
