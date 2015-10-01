package org.app4j.site.runtime.route;

import org.app4j.site.Module;
import org.app4j.site.runtime.InternalModule;
import org.app4j.site.runtime.error.ErrorConfig;
import org.app4j.site.web.Handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class RouteConfig extends InternalModule {
    private final Map<String, Route<Handler>> routes = new HashMap<>();

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(ErrorConfig.class);
    }

    public RouteConfig get(String route, Handler handler) {
        route("GET").add(route, handler);
        return this;
    }

    public RouteConfig post(String route, Handler handler) {
        route("POST").add(route, handler);
        return this;
    }

    public RouteConfig put(String route, Handler handler) {
        route("PUT").add(route, handler);
        return this;
    }

    public RouteConfig delete(String route, Handler handler) {
        route("DELETE").add(route, handler);
        return this;
    }

    public Handler find(String method, String path, Map<String, String> parameters) {
        Route<Handler> route = route(method);
        if (route == null) {
            throw new NotFoundException(path);
        }
        return route.find(path, parameters);
    }

    private Route<Handler> route(String method) {
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
