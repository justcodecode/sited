package org.app4j.site.runtime.route;

import org.app4j.site.Site;
import org.app4j.site.runtime.SiteModule;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chi
 */
public class RouteModule extends SiteModule implements RouteConfig {
    private final Map<Request.Method, Route<Handler>> routes = new HashMap<>();

    public RouteModule(Site site) {
        super(site);
    }

    @Override
    public RouteModule get(String route, Handler handler) {
        route(Request.Method.GET).add(route, handler);
        return this;
    }

    @Override
    public RouteModule post(String route, Handler handler) {
        route(Request.Method.POST).add(route, handler);
        return this;
    }

    @Override
    public RouteModule put(String route, Handler handler) {
        route(Request.Method.PUT).add(route, handler);
        return this;
    }

    @Override
    public RouteModule delete(String route, Handler handler) {
        route(Request.Method.DELETE).add(route, handler);
        return this;
    }

    @Override
    public Handler find(Request.Method method, String path, Map<String, String> parameters) {
        Route<Handler> route = route(method);
        if (route == null) {
            throw new NotFoundException(path);
        }
        return route.find(path, parameters);
    }

    private Route<Handler> route(Request.Method method) {
        Route<Handler> route = routes.get(method);
        if (route == null) {
            route = new Route<>();
            routes.put(method, route);
        }
        return route;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void configure() throws Exception {
        bind(RouteConfig.class).to(this).export();
    }
}
