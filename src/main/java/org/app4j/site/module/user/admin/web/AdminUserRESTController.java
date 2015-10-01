package org.app4j.site.module.user.admin.web;

import org.app4j.site.module.user.domain.User;
import org.app4j.site.module.user.service.UserService;
import org.app4j.site.runtime.route.NotFoundException;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.IOException;

/**
 * @author chi
 */
public class AdminUserRESTController {
    private final UserService userService;

    public AdminUserRESTController(UserService userService) {
        this.userService = userService;
    }

    public Response findByUsername(Request request) throws IOException {
        String name = request.query("username").get();

        User user = userService.findByUsername(name);
        if (user == null) {
            throw new NotFoundException(request.path());
        }
        user.setPassword(null);
        return Response.bean(user);
    }

    public Response findUsers(Request request) throws IOException {
        int offset = request.query("offset", Integer.class).orElse(0).get();
        int fetchSize = request.query("fetchSize", Integer.class).orElse(0).get();

        return Response.bean(userService.findUsers(offset, fetchSize));
    }

    public Response updateUser(Request request) throws IOException {
        User user = request.body(User.class);
        if (user.getId() != null) {
            userService.update(user);
        } else {
            userService.insert(user);
        }

        return Response.empty();
    }

    public Response deleteUser(Request request) throws IOException {
        userService.delete(request.path(":id").get());
        return Response.empty();
    }
}
