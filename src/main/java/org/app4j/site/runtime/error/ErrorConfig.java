package org.app4j.site.runtime.error;

import com.google.common.collect.Maps;
import org.app4j.site.runtime.InternalModule;
import org.app4j.site.web.Handler;

import java.util.Map;

/**
 * @author chi
 */
public class ErrorConfig extends InternalModule {
    private final Map<Class, Handler> handlers = Maps.newHashMap();

    public ErrorConfig handle(Class<? extends Exception> exceptionType, Handler handler) {
        handlers.put(exceptionType, handler);
        return this;
    }

    public Handler handler(Class<? extends Exception> exceptionType) {
        Handler handler = handlers.get(exceptionType);
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
