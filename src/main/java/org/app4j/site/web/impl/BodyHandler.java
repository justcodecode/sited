package org.app4j.site.web.impl;

import java.io.InputStream;

/**
 * @author chi
 */
public interface BodyHandler {
    InputStream handle(ResponseImpl response);
}
