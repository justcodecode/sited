package org.app4j.site.web.impl;

import java.io.InputStream;

/**
 * @author neo
 */
public interface BodyHandler {
    InputStream handle(ResponseImpl response);
}
