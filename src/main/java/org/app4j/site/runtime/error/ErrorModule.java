package org.app4j.site.runtime.error;

import com.google.common.collect.Maps;
import org.app4j.site.runtime.InternalModule;

import java.util.Map;

/**
 * @author chi
 */
public class ErrorModule extends InternalModule implements ErrorConfig {
    private final Map<Class, ErrorHandler> handlers = Maps.newHashMap();

    @Override
    protected void configure() throws Exception {
        bind(ErrorConfig.class).to(this).export();
    }

    @Override
    public ErrorConfig on(Class<? extends Throwable> error, ErrorHandler errorHandler) {
        handlers.put(error, errorHandler);
        return this;
    }

    @Override
    public ErrorHandler handler(Class<? extends Throwable> error) {
        ErrorHandler handler = handlers.get(error);
        if (handler == null) {
            handlers.get(Exception.class);
        }
        return handler;
    }
}
