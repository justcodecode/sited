package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.spi.LoggerFactoryBinder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chi
 */
public final class StaticLoggerBinder implements LoggerFactoryBinder, ILoggerFactory {
    public static final String REQUESTED_API_VERSION = "1.6";
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
    private final Map<String, Logger> loggers = new ConcurrentHashMap<>();

    private StaticLoggerBinder() {
    }

    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return this;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return StaticLoggerBinder.class.getName();
    }

    @Override
    public Logger getLogger(String name) {
        return loggers.computeIfAbsent(name, this::createLogger);
    }

    private Logger createLogger(String name) {
        if (name.startsWith("com.mchange")
            || name.startsWith("org.elasticsearch")
            || name.startsWith("org.mongodb")
            || name.startsWith("org.xnio")
            || name.startsWith("org.thymeleaf")
            || name.startsWith("org.apache")) {
            return new LoggerImpl(name, Message.Level.WARNING);
        } else {
            return new LoggerImpl(name, Message.Level.DEBUG);
        }
    }
}
