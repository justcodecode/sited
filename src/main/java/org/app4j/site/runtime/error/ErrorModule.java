package org.app4j.site.runtime.error;

import com.google.common.collect.Maps;
import org.app4j.site.Site;
import org.app4j.site.runtime.SiteModule;
import org.app4j.site.web.Response;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author chi
 */
public class ErrorModule extends SiteModule implements ErrorConfig {
    private final Map<Class, ErrorHandler> handlers = Maps.newHashMap();

    public ErrorModule(Site site) {
        super(site);
    }

    @Override
    protected void configure() throws Exception {
        bind(ErrorConfig.class).to(this).export();

        on(Throwable.class, (request, e) -> {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            return Response.text(stringWriter.toString()).setContentType("text/plain").setStatusCode(500);
        });
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
            return handlers.get(Throwable.class);
        }
        return handler;
    }
}
