package org.app4j.site.web.exception;

/**
 * @author chi
 */
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 8663360723004690205L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
