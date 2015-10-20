package org.app4j.site.module.user.web.admin;

import org.app4j.site.module.user.service.PermissionService;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.IOException;

/**
 * @author chi
 */
public class AdminPermissionRESTController {
    private final PermissionService permissionService;

    public AdminPermissionRESTController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public Response list(Request request) throws IOException {
        int offset = request.query("offset", Integer.class).orElse(0).get();
        int fetchSize = request.query("fetchSize", Integer.class).orElse(0).get();
        return Response.bean(permissionService.listPermissions(offset, fetchSize));
    }
}
