package org.app4j.site.web.exception;

/**
 * @author chi
 */
public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 5545181864430282120L;

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
