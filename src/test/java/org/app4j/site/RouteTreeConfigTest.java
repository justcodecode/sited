package org.app4j.site;

import org.app4j.site.internal.route.RouteModule;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * @author chi
 */
public class RouteTreeConfigTest {
    RouteModule routeConfig = new RouteModule(null);

    @Test
    public void get() {
        routeConfig.get("/some/", (request) -> Response.empty());
        Assert.assertTrue(routeConfig.find(Request.Method.GET, "/some/").isPresent());
    }

    @Test
    public void parameter() {
        routeConfig.get("/some/:id", (request) -> Response.empty());
        Optional<Route> route = routeConfig.find(Request.Method.GET, "/some/1");
        Assert.assertTrue(route.isPresent());
        Assert.assertEquals("1", route.get().parameters.get(":id"));
    }
}
