package org.app4j.site.runtime.admin;

import org.app4j.site.runtime.admin.service.Console;
import org.app4j.site.runtime.route.RouteConfig;

/**
 * @author chi
 */
public interface AdminConfig {
    Console console();

    RouteConfig route();
}
