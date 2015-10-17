package org.app4j.site;

import io.undertow.Undertow;
import io.undertow.server.handlers.GracefulShutdownHandler;
import org.app4j.site.module.page.PageModule;
import org.app4j.site.module.user.UserModule;
import org.app4j.site.web.impl.SiteHandler;

import java.util.Arrays;
import java.util.List;

/**
 * @author chi
 */
public class Sited {
    private final Site site;
    private final Undertow server;

    public Sited(Site site) {
        this.site = site;
        scanModules().forEach(site::install);
        server = Undertow.builder()
            .addHttpListener(site.port(),
                site.host())
            .setHandler(new GracefulShutdownHandler(new SiteHandler(site))).build();
    }

    public static String usage() {
        return "usage: sited dir";
    }

    protected List<Class<? extends Module>> scanModules() {
        return Arrays.asList(PageModule.class, UserModule.class);
    }

    public void start() {
        site.start();
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void stop() {
        server.stop();
        site.stop();
    }
}
