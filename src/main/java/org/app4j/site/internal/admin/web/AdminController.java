package org.app4j.site.internal.admin.web;

import com.google.common.collect.Maps;
import org.app4j.site.Site;
import org.app4j.site.internal.admin.service.AdminUser;
import org.app4j.site.internal.admin.service.InstallService;
import org.app4j.site.internal.scheduler.Event;
import org.app4j.site.internal.template.AssetsConfig;
import org.app4j.site.util.Resource;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;
import org.app4j.site.web.exception.UnauthorizedException;

import java.io.IOException;
import java.util.Date;

/**
 * @author chi
 */
public class AdminController {
    private final Site site;
    private final InstallService installService;
    private final AssetsConfig assetsConfig;

    public AdminController(Site site, InstallService installService, AssetsConfig assetsConfig) {
        this.site = site;
        this.installService = installService;
        this.assetsConfig = assetsConfig;
    }

    public Response index(Request request) {
        if (installService.isInstalled()) {
            AdminUser adminUser = request.require(AdminUser.class);
            if (adminUser == null) {
                throw new UnauthorizedException("require admin user");
            }

            if (site.isAdminEnabled()) {
                Resource resource = assetsConfig.get("/admin/index.html").get();
                return Response.pipe(resource.openStream())
                    .setContentType("text/html")
                    .setStatusCode(200);
            } else {
                throw new NotFoundException(request.path());
            }
        } else {
            return Response.redirect("/admin/install.html");
        }
    }

    public Response install(Request request) {
        if (installService.isInstalled()) {
            throw new NotFoundException(request.path());
        }
        Resource resource = assetsConfig.get("/admin/install.html").get();
        return Response.pipe(resource.openStream())
            .setContentType("text/html")
            .setStatusCode(200);
    }

    public Response profile(Request request) throws IOException {
        if (installService.isInstalled()) {
            throw new NotFoundException(request.path());
        }

        ProfileRequest profileRequest = request.body(ProfileRequest.class);
        installService.installed(new Date());

        site.scheduler().trigger(new Event<>(new AdminUser(profileRequest.getAdminUsername(), profileRequest.getAdminEmail(), profileRequest.getAdminPassword()), Maps.newHashMap()));
        return Response.redirect("/admin/index.html");
    }
}
