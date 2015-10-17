package org.app4j.site.module.user;

import com.google.common.collect.Lists;
import org.app4j.site.Module;
import org.app4j.site.Site;
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
import org.app4j.site.runtime.scheduler.Event;
import org.app4j.site.runtime.scheduler.EventHandler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.UnauthorizedException;

import java.util.Date;
import java.util.Optional;

/**
 * @author chi
 */
public class UserModule extends Module {
    public UserModule(Site site) {
        super(site);
    }

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
                return new AdminUser(user.getUsername(), user.getEmail(), null);
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

        scheduler().on(AdminUser.class, new EventHandler<AdminUser>() {
            @Override
            public void on(Event<AdminUser> event) {
                AdminUser adminUser = event.target();
                User user = new User();
                user.setUsername(adminUser.username());
                user.setEmail(adminUser.email());
                user.setPassword(adminUser.password());
                user.setCreateTime(new Date());
                user.setLastUpdateTime(new Date());
                user.setStatus(1);

                user.setRoles(Lists.newArrayList("admin"));
                userService.save(user);
            }
        });


        error().on(UnauthorizedException.class, (request, e) -> {
            if ("application/json".equalsIgnoreCase(request.accept())) {
                return Response.empty().setStatusCode(401);
            }
            return Response.redirect("/login.html?from=" + request.path());
        });
    }

}
