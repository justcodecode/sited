package org.app4j.site.module.user.admin.web;

import org.app4j.site.module.user.domain.Role;
import org.app4j.site.module.user.service.RoleService;
import org.app4j.site.runtime.route.NotFoundException;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.IOException;

/**
 * @author chi
 */
public class AdminRoleRESTController {
    private final RoleService roleService;

    public AdminRoleRESTController(RoleService roleService) {
        this.roleService = roleService;
    }

    public Response findByUsername(Request request) throws IOException {
        String name = request.query("username").get();

        Role role = roleService.findByName(name);
        if (role == null) {
            throw new NotFoundException(request.path());
        }
        return Response.bean(role);
    }

    public Response list(Request request) throws IOException {
        int offset = request.query("offset", Integer.class).orElse(0).get();
        int fetchSize = request.query("fetchSize", Integer.class).orElse(0).get();
        return Response.bean(roleService.listRoles(offset, fetchSize));
    }

    public Response update(Request request) throws IOException {
        Role role = request.body(Role.class);
        if (role.getId() != null) {
            roleService.update(role);
        } else {
            roleService.insert(role);
        }
        return Response.empty();
    }

    public Response delete(Request request) throws IOException {
        roleService.delete(request.query("id").get());
        return Response.empty();
    }
}
