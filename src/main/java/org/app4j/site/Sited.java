package org.app4j.site;

import com.google.common.base.Stopwatch;
import io.undertow.Undertow;
import io.undertow.server.handlers.GracefulShutdownHandler;
import org.app4j.site.module.page.PageModule;
import org.app4j.site.module.user.UserModule;
import org.app4j.site.web.impl.SiteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public class Sited {
    private final Logger logger = LoggerFactory.getLogger(Sited.class);
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
        Stopwatch stopwatch = Stopwatch.createStarted();
        site.start();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        logger.info("%s", site);
        logger.info("site started in %sms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        server.start();

    }

    public void stop() {
        server.stop();
        site.stop();
    }
}
