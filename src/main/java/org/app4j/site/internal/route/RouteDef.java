package org.app4j.site.internal.route;

import org.app4j.site.web.Handler;

/**
 * @author chi
 */
public class RouteDef {
    public final String route;
    public final Handler handler;

    public RouteDef(String route, Handler handler) {
        this.route = route;
        this.handler = handler;
    }
}
