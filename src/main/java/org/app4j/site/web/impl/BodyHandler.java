package org.app4j.site.web.impl;

import java.io.InputStream;

/**
 * @author chi
 */
public interface BodyHandler<T extends Body> {
    InputStream handle(T body);
}
