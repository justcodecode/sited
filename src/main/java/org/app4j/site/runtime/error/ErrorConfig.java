package org.app4j.site.runtime.error;

import com.google.common.collect.Maps;
import org.app4j.site.runtime.InternalModule;

import java.util.Map;

/**
 * @author chi
 */
public class ErrorConfig extends InternalModule {
    private final Map<Class, ErrorHandler> handlers = Maps.newHashMap();

    public ErrorConfig handle(Class<? extends Exception> exceptionType, ErrorHandler handler) {
        handlers.put(exceptionType, handler);
        return this;
    }

    public ErrorHandler handler(Class<? extends Exception> exceptionType) {
        ErrorHandler handler = handlers.get(exceptionType);
        if (handler == null) {
            handlers.get(Exception.class);
        }
        return handler;
    }

    @Override
    protected void configure() throws Exception {
        bind(ErrorConfig.class).to(this).export();
    }

}
