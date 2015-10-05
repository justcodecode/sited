package org.app4j.site.module.user.web;

import org.app4j.site.module.user.domain.User;
import org.app4j.site.module.user.service.UserService;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.UnauthorizedException;

import java.io.IOException;
import java.util.Optional;

/**
 * @author chi
 */
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public Response login(Request request) throws IOException {
        LoginRequest loginRequest = request.body(LoginRequest.class);
        Optional<User> userOptional = userService.findByUsername(loginRequest.getUsername());
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(loginRequest.getPassword())) {
            String toURL = loginRequest.getToURL() == null ? "/" : loginRequest.getToURL();
            return Response.redirect(toURL).setCookie("uid", userOptional.get().getId());
        }
        throw new UnauthorizedException("require user login");
    }

    public Response register(Request request) throws IOException {
        //TODO
        return Response.empty();
    }

    public Response logout(Request request) throws IOException {
        return Response.empty();
    }
}
