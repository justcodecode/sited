package org.app4j.site.runtime.route;


/**
 * @author chi
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String path) {
        super(path);
    }
}
