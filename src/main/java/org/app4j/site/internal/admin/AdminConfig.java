package org.app4j.site.internal.admin;

import org.app4j.site.internal.admin.service.Console;
import org.app4j.site.internal.route.RouteConfig;

/**
 * @author chi
 */
public interface AdminConfig {
    Console console();

    RouteConfig route();
}
