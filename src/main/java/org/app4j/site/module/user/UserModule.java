package org.app4j.site.module.user;

import com.google.common.collect.Lists;
import org.app4j.site.Module;
import org.app4j.site.Site;
import org.app4j.site.internal.admin.service.AdminUser;
import org.app4j.site.internal.event.Event;
import org.app4j.site.internal.event.EventHandler;
import org.app4j.site.module.user.domain.User;
import org.app4j.site.module.user.service.PermissionService;
import org.app4j.site.module.user.service.RoleService;
import org.app4j.site.module.user.service.UserService;
import org.app4j.site.module.user.service.codec.PermissionCodec;
import org.app4j.site.module.user.service.codec.RoleCodec;
import org.app4j.site.module.user.service.codec.UserCodec;
import org.app4j.site.module.user.web.UserRESTController;
import org.app4j.site.module.user.web.admin.AdminUserRESTController;
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
            if (userOptional.isPresent() && userOptional.get().roles.contains("admin")) {
                User user = userOptional.get();
                return new AdminUser(user.username, user.email, null);
            }
            return null;
        }).export();

        UserRESTController userRESTController = new UserRESTController(userService);
        route().post("/login", userRESTController::login)
            .get("/logout", userRESTController::logout);

        event().on(AdminUser.class, new EventHandler<AdminUser>() {
            @Override
            public void on(Event<AdminUser> event) {
                AdminUser adminUser = event.target;
                User user = new User();
                user.username = adminUser.username();
                user.email = adminUser.email();
                user.password = adminUser.password();
                user.createTime = new Date();
                user.lastUpdateTime = new Date();
                user.status = 1;
                user.roles = Lists.newArrayList("admin");
                userService.save(user);
            }
        });


        error().on(UnauthorizedException.class, (request, e) -> {
            if (request.accept().contains("application/json")) {
                return Response.empty().setStatusCode(401);
            }
            return Response.redirect("/login.html?from=" + request.path());
        });

        //Admin
        AdminUserRESTController adminUserRESTController = new AdminUserRESTController(userService);
        admin().route()
            .get("/admin/api/user/:username", adminUserRESTController::findByUsername)
            .get("/admin/api/user/", adminUserRESTController::findUsers);
    }


}
