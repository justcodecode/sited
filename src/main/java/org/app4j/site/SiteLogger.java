package org.app4j.site;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import org.slf4j.LoggerFactory;

/**
 * @author chi
 */
public class SiteLogger {
    static {
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    public SiteLogger() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger mongodbLogger = loggerContext.getLogger("org.mongodb");
        mongodbLogger.setLevel(Level.WARN);

        Logger undertowLogger = loggerContext.getLogger("io.undertow");
        undertowLogger.setLevel(Level.WARN);

        Logger xnioLogger = loggerContext.getLogger("org.xnio");
        xnioLogger.setLevel(Level.WARN);

        Logger thymeleafLogger = loggerContext.getLogger("org.thymeleaf");
        thymeleafLogger.setLevel(Level.WARN);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        encoder.start();
    }

    public SiteLogger setLevel(Level level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = loggerContext.getLogger("ROOT");
        root.setLevel(level);
        return this;
    }
}
