package org.app4j.site.module.user.web.admin;

import org.app4j.site.module.user.domain.User;
import org.app4j.site.module.user.service.UserService;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;

import java.io.IOException;
import java.util.Optional;

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

        Optional<User> userOptional = userService.findByUsername(name);
        if (!userOptional.isPresent()) {
            throw new NotFoundException(request.path());
        }
        User user = userOptional.get();
        user.password = null;
        return Response.bean(user);
    }

    public Response findUsers(Request request) throws IOException {
        int offset = request.query("offset", Integer.class).orElse(0).get();
        int fetchSize = request.query("fetchSize", Integer.class).orElse(0).get();

        return Response.bean(userService.findUsers(offset, fetchSize));
    }

    public Response updateUser(Request request) throws IOException {
        User user = request.body(User.class);
        if (user.id != null) {
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
