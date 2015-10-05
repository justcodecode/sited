package org.app4j.site.web.exception;

/**
 * @author chi
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
