package org.app4j.site;

import com.google.common.collect.Lists;
import io.undertow.Undertow;
import io.undertow.server.handlers.GracefulShutdownHandler;
import org.app4j.site.module.page.PageModule;
import org.app4j.site.web.impl.SiteHandler;

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
                .addHttpListener(8080, "0.0.0.0")
                .setHandler(new GracefulShutdownHandler(new SiteHandler(site))).build();
    }

    public static String usage() {
        return "usage: sited --dir=xxx --db=xxx";
    }

    protected List<Class<? extends Module>> scanModules() {
        return Lists.newArrayList(PageModule.class);
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
