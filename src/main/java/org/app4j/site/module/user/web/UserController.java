package org.app4j.site.module.user.web;

import org.app4j.site.module.user.service.UserService;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.IOException;

/**
 * @author chi
 */
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public Response login(Request request) throws IOException {
        return Response.empty();
    }

    public Response register(Request request) throws IOException {
        //TODO
        return Response.empty();
    }

    public Response logout(Request request) throws IOException {
        return Response.empty();
    }
}
