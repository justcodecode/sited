package org.app4j.site.runtime.admin.web;

import org.app4j.site.Site;
import org.app4j.site.runtime.admin.service.AdminUser;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;
import org.app4j.site.web.exception.UnauthorizedException;

/**
 * @author chi
 */
public class AdminHandler implements Handler {
    private final Site site;
    private final Handler handler;

    public AdminHandler(Site site, Handler handler) {
        this.site = site;
        this.handler = handler;
    }

    @Override
    public Response handle(Request request) throws Exception {
        AdminUser adminUser = request.require(AdminUser.class);

        if (adminUser == null) {
            throw new UnauthorizedException("require admin");
        }

        if (site.isAdminEnabled()) {
            return handler.handle(request);
        } else {
            throw new NotFoundException(request.path());
        }
    }
}
