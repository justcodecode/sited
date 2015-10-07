package org.app4j.site.runtime.admin.web;

import com.google.common.collect.Maps;
import org.app4j.site.Site;
import org.app4j.site.runtime.admin.domain.Profile;
import org.app4j.site.runtime.admin.service.AdminUser;
import org.app4j.site.runtime.admin.service.ProfileService;
import org.app4j.site.runtime.event.Event;
import org.app4j.site.runtime.template.AssetsConfig;
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
    private final ProfileService profileService;
    private final AssetsConfig assetsConfig;

    public AdminController(Site site, ProfileService profileService, AssetsConfig assetsConfig) {
        this.site = site;
        this.profileService = profileService;
        this.assetsConfig = assetsConfig;
    }

    public Response index(Request request) {
        if (profileService.isInstalled()) {
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
        if (profileService.isInstalled()) {
            throw new NotFoundException(request.path());
        }
        Resource resource = assetsConfig.get("/admin/install.html").get();
        return Response.pipe(resource.openStream())
                .setContentType("text/html")
                .setStatusCode(200);
    }

    public Response profile(Request request) throws IOException {
        if (profileService.isInstalled()) {
            throw new NotFoundException(request.path());
        }

        ProfileRequest profileRequest = request.body(ProfileRequest.class);

        Profile profile = new Profile();
        profile.setCharset(profileRequest.getCharset());
        profile.setLocale(profileRequest.getLocale());
        profile.setCreateTime(new Date());
        profile.setLastUpdateTime(new Date());
        profile.setStatus(1);

        profileService.save(profile);

        site.event().trigger(new Event<>(new AdminUser(profileRequest.getAdminUsername(), profileRequest.getAdminEmail(), profileRequest.getAdminPassword()), Maps.newHashMap()));
        return Response.redirect("/admin/index.html");
    }
}
