package org.app4j.site.internal.route;

import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;

import java.util.Optional;

/**
 * @author chi
 */
public interface RouteConfig {
    RouteConfig get(String route, Handler handler);

    RouteConfig post(String route, Handler handler);

    RouteConfig put(String route, Handler handler);

    RouteConfig delete(String route, Handler handler);

    Optional<Route> find(Request.Method method, String path);
}
