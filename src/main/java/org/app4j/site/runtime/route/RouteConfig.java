package org.app4j.site.runtime.route;

import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;

import java.util.Map;

/**
 * @author chi
 */
public interface RouteConfig {
    RouteConfig get(String route, Handler handler);

    RouteConfig post(String route, Handler handler);

    RouteConfig put(String route, Handler handler);

    RouteConfig delete(String route, Handler handler);

    Handler find(Request.Method method, String path, Map<String, String> parameters);
}
