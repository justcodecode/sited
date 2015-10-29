package org.app4j.site;

import com.google.common.base.Charsets;
import com.google.common.base.Stopwatch;
import io.undertow.Undertow;
import io.undertow.server.handlers.GracefulShutdownHandler;
import org.app4j.site.module.page.PageModule;
import org.app4j.site.module.user.UserModule;
import org.app4j.site.web.impl.SiteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public class Sited {
    private final Logger logger = LoggerFactory.getLogger(Sited.class);

    private final Site site;
    private final Undertow server;

    public Sited() {
        try {
            site = new Site(properties());
        } catch (IOException e) {
            throw new Error(e);
        }
        scanModules().forEach(site::install);
        server = Undertow.builder()
            .addHttpListener(site.port(),
                site.host())
            .setHandler(new GracefulShutdownHandler(new SiteHandler(site))).build();
    }

    public Sited install(Class<? extends Module> moduleClass) {
        site.install(moduleClass);
        return this;
    }

    private Properties properties() throws IOException {
        InputStream inputStream;
        if (System.getProperties().containsKey("site.conf")) {
            File file = new File(System.getProperty("site.conf"));
            inputStream = new FileInputStream(file);
            logger.info("use %s", file.getAbsolutePath());
        } else {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("site.properties");
            logger.info("use classpath site.properties");
        }
        Properties properties = new Properties();
        try (InputStreamReader reader = new InputStreamReader(inputStream, Charsets.UTF_8)) {
            properties.load(reader);
        }
        return properties;
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
        logger.info("site stopped");
    }
}
