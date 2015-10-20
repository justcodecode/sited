package org.app4j.site.module.user.web.admin;

import org.app4j.site.module.user.domain.Role;
import org.app4j.site.module.user.service.RoleService;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;

import java.io.IOException;

/**
 * @author chi
 */
public class AdminRoleRESTController {
    private final RoleService roleService;

    public AdminRoleRESTController(RoleService roleService) {
        this.roleService = roleService;
    }

    public Response findById(Request request) throws IOException {
        String name = request.path(":id").get();

        Role role = roleService.findByName(name);
        if (role == null) {
            throw new NotFoundException(request.path());
        }
        return Response.bean(role);
    }

    public Response findByUsername(Request request) throws IOException {
        String name = request.path(":username").get();

        Role role = roleService.findByName(name);
        if (role == null) {
            throw new NotFoundException(request.path());
        }
        return Response.bean(role);
    }

    public Response findRoles(Request request) throws IOException {
        int offset = request.query("offset", Integer.class).orElse(0).get();
        int fetchSize = request.query("fetchSize", Integer.class).orElse(0).get();
        return Response.bean(roleService.listRoles(offset, fetchSize));
    }

    public Response createRole(Request request) throws IOException {
        Role role = request.body(Role.class);
        roleService.save(role);
        return Response.bean(role);
    }

    public Response updateRole(Request request) throws IOException {
        Role role = request.body(Role.class);
        roleService.update(role);
        return Response.empty();
    }

    public Response deleteRole(Request request) throws IOException {
        roleService.delete(request.query("id").get());
        return Response.empty();
    }
}
