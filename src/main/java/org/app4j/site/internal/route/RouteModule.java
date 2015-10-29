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
    private final Map<Request.Method, Router> routes = new HashMap<>();

    public RouteModule(Site site) {
        super(site);
    }

    @Override
    public RouteModule get(String route, Handler handler) {
        router(Request.Method.GET).add(route, handler);
        return this;
    }

    @Override
    public RouteModule post(String route, Handler handler) {
        router(Request.Method.POST).add(route, handler);
        return this;
    }

    @Override
    public RouteModule put(String route, Handler handler) {
        router(Request.Method.PUT).add(route, handler);
        return this;
    }

    @Override
    public RouteModule delete(String route, Handler handler) {
        router(Request.Method.DELETE).add(route, handler);
        return this;
    }

    @Override
    public Optional<Router.Route> find(Request.Method method, String path) {
        return router(method).find(path);
    }

    private synchronized Router router(Request.Method method) {
        Router router = routes.get(method);
        if (router == null) {
            router = new Router();
            routes.put(method, router);
        }
        return router;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void configure() throws Exception {
        bind(RouteConfig.class).to(this).export();
    }
}
