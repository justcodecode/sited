package org.app4j.site.module.user;

import org.app4j.site.Module;
import org.app4j.site.module.user.admin.web.AdminUserRESTController;
import org.app4j.site.module.user.codec.PermissionCodec;
import org.app4j.site.module.user.codec.RoleCodec;
import org.app4j.site.module.user.codec.UserCodec;
import org.app4j.site.module.user.domain.User;
import org.app4j.site.module.user.service.PermissionService;
import org.app4j.site.module.user.service.RoleService;
import org.app4j.site.module.user.service.UserService;
import org.app4j.site.module.user.web.UserController;
import org.app4j.site.runtime.admin.service.AdminUser;
import org.app4j.site.web.Request;

import java.util.Optional;

/**
 * @author chi
 */
public class UserModule extends Module {
    @Override
    protected void configure() throws Exception {
        site().database().codecs()
                .add(new UserCodec())
                .add(new RoleCodec())
                .add(new PermissionCodec());

        UserService userService = new UserService(site().database().get());
        bind(UserService.class).to(userService);

        RoleService roleService = new RoleService(site().database().get());
        bind(RoleService.class).to(roleService);

        PermissionService permissionService = new PermissionService(site().database().get());
        bind(PermissionService.class).to(permissionService);

        bind(AdminUser.class).to((key, scope) -> {
            Request request = scope.require(Request.class);
            Optional<User> userOptional = userService.user(request);
            if (userOptional.isPresent() && userOptional.get().hasRole("admin")) {
                User user = userOptional.get();
                return new AdminUser(user.getUsername(), user.getEmail());
            }
            return null;
        }).export();

        UserController userController = new UserController(userService);
        route().post("/login", userController::login)
                .get("/logout", userController::logout);

        //Admin
        AdminUserRESTController adminUserRESTController = new AdminUserRESTController(userService);
        admin().route()
                .get("/admin/api/user/:username", adminUserRESTController::findByUsername)
                .get("/admin/api/user/", adminUserRESTController::findUsers);
    }

}
