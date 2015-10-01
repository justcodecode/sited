package org.app4j.site;

import com.google.common.collect.Maps;
import org.app4j.site.runtime.route.RouteConfig;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author chi
 */
public class RouteConfigTest {
    RouteConfig routeConfig = new RouteConfig();

    @Test
    public void get() {
        routeConfig.get("/some/", (request) -> Response.empty());
        Assert.assertNotNull(routeConfig.find(Request.Method.GET, "/some/", Maps.<String, String>newHashMap()));
    }

    @Test
    public void parameter() {
        Map<String, String> parameters = Maps.newHashMap();
        routeConfig.get("/some/:id", (request) -> Response.empty());
        routeConfig.find(Request.Method.GET, "/some/1", parameters);
        Assert.assertEquals("1", parameters.get(":id"));
    }
}
