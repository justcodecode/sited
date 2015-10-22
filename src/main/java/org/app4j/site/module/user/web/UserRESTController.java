package org.app4j.site.module.user.web;

import com.google.common.collect.Lists;
import org.app4j.site.module.user.domain.User;
import org.app4j.site.module.user.service.UserService;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.BadRequestException;
import org.app4j.site.web.exception.UnauthorizedException;

import java.io.IOException;
import java.util.Optional;

/**
 * @author chi
 */
public class UserRESTController {
    private final UserService userService;

    public UserRESTController(UserService userService) {
        this.userService = userService;
    }

    public Response login(Request request) throws IOException {
        UserLoginRequest userLoginRequest = request.body(UserLoginRequest.class);
        Optional<User> user = userService.findByUsername(userLoginRequest.getUsername());
        if (!user.isPresent() || !user.get().password.equals(userLoginRequest.getPassword())) {
            throw new UnauthorizedException("invalid username or password");
        }
        return Response.bean(user.get()).setCookie(User.COOKIE_NAME, userService.encodeUser(user.get()));
    }

    public Response register(Request request) throws IOException {
        UserRegisterRequest userRegisterRequest = request.body(UserRegisterRequest.class);
        if (userService.isUsernameOrEmailExists(userRegisterRequest.username, userRegisterRequest.email)) {
            throw new BadRequestException("username or email exist", null, null);
        }
        User user = new User();
        user.username = userRegisterRequest.username;
        user.email = userRegisterRequest.email;
        user.password = userRegisterRequest.password;
        user.roles = Lists.newArrayList("L1");
        userService.save(user);
        return Response.bean(user);
    }

    public Response logout(Request request) throws IOException {
        return Response.empty();
    }
}
