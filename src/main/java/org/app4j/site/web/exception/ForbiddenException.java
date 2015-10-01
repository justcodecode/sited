package org.app4j.site.web.exception;

/**
 * @author chi
 */
public class ForbiddenException extends RuntimeException {
    private static final long serialVersionUID = 5472429043879214361L;

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
