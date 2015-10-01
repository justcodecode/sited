package org.app4j.site;

import com.google.common.collect.Maps;
import org.app4j.site.runtime.route.RouteConfig;
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
        routeConfig.get("/some/", (request, response) -> {
        });

        Assert.assertNotNull(routeConfig.find("GET", "/some/", Maps.<String, String>newHashMap()));
    }

    @Test
    public void parameter() {
        Map<String, String> parameters = Maps.newHashMap();
        routeConfig.get("/some/:id", (request, response) -> {
        });

        routeConfig.find("GET", "/some/1", parameters);
        Assert.assertEquals("1", parameters.get(":id"));
    }
}
