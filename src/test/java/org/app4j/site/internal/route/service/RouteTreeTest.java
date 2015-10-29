package org.app4j.site.internal.route.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * @author chi
 */
public class RouteTreeTest {
    @Test
    public void get() {
        RouteTree routeTree = new RouteTree();
        routeTree.add("/product/:id(\\d+).html", request -> null);

        Optional<Route> route = routeTree.find("/product/1.html");
        Assert.assertTrue(route.isPresent());
        Assert.assertEquals("1", route.get().parameters.get(":id"));
    }
}