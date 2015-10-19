package org.app4j.site.internal.route.service;

import com.google.common.collect.Maps;
import org.app4j.site.internal.route.Route;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author chi
 */
public class RouteTest {
    @Test
    public void get() {
        Route<Object> route = new Route<>();
        route.add("/product/:id(\\d+).html", new Object());

        HashMap<String, String> parameters = Maps.newHashMap();
        Object o = route.find("/product/1.html", parameters);
        Assert.assertNotNull(o);
        Assert.assertEquals("1", parameters.get(":id"));
    }
}