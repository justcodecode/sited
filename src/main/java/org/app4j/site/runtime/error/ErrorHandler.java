package org.app4j.site.runtime.error;

import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

/**
 * @author chi
 */
public interface ErrorHandler {
    Response handle(Request request, Throwable e);
}
