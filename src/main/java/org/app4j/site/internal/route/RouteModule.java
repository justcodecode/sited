package org.app4j.site.internal.route;

import org.app4j.site.Site;
import org.app4j.site.internal.InternalModule;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class RouteModule extends InternalModule implements RouteConfig {
    private final Map<Request.Method, RouteTree> routes = new HashMap<>();

    public RouteModule(Site site) {
        super(site);
    }

    @Override
    public RouteModule get(String route, Handler handler) {
        method(Request.Method.GET).add(route, new RouteDef(route, handler));
        return this;
    }

    @Override
    public RouteModule post(String route, Handler handler) {
        method(Request.Method.POST).add(route, new RouteDef(route, handler));
        return this;
    }

    @Override
    public RouteModule put(String route, Handler handler) {
        method(Request.Method.PUT).add(route, new RouteDef(route, handler));
        return this;
    }

    @Override
    public RouteModule delete(String route, Handler handler) {
        method(Request.Method.DELETE).add(route, new RouteDef(route, handler));
        return this;
    }

    @Override
    public Optional<Route> find(Request.Method method, String path) {
        RouteTree routeTree = method(method);
        if (routeTree == null) {
            return Optional.empty();
        }

        return routeTree.find(path);
    }

    private synchronized RouteTree method(Request.Method method) {
        RouteTree routeTree = routes.get(method);
        if (routeTree == null) {
            routeTree = new RouteTree();
            routes.put(method, routeTree);
        }
        return routeTree;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void configure() throws Exception {
        bind(RouteConfig.class).to(this).export();
    }
}
