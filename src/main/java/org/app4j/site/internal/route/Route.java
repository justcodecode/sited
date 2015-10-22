package org.app4j.site.internal.route;

import java.util.Map;

/**
 * @author chi
 */
public class Route {
    public final RouteDef def;
    public final Map<String, String> parameters;

    public Route(RouteDef def, Map<String, String> parameters) {
        this.def = def;
        this.parameters = parameters;
    }
}
