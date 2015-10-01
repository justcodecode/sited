package org.app4j.site.web.impl;

import io.undertow.io.Sender;

/**
 * @author neo
 */
public interface BodyHandler {
    void handle(ResponseImpl response, Sender sender, RequestImpl request);
}
