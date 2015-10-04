package org.app4j.site.web.impl;

import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.app4j.site.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;

/**
 * @author chi
 */
public class SiteHandler implements HttpHandler {
    private final Logger logger = LoggerFactory.getLogger(SiteHandler.class);
    private final Map<Class, BodyHandler> handlers = Maps.newHashMap();
    private final Site site;

    public SiteHandler(Site site) {
        this.site = site;
        handlers.put(BeanBody.class, new BeanBodyResponseHandler());
        handlers.put(TextBody.class, new TextBodyResponseHandler());
        handlers.put(ByteArrayBody.class, new ByteArrayBodyResponseHandler());
        handlers.put(FileBody.class, new FileBodyResponseHandler());
        handlers.put(TemplateBody.class, new TemplateBodyResponseHandler(site));
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        try {
            RequestImpl request = new RequestImpl(exchange, site);
            ResponseImpl response = (ResponseImpl) site.handle(request);

            BodyHandler handler = handlers.get(response.body.getClass());
            if (handler == null)
                throw new Error(String.format("unexpected body class, body=%s", response.body.getClass().getCanonicalName()));

            try (InputStream inputStream = handler.handle(response)) {
                exchange.setStatusCode(response.statusCode);
                response.cookies.forEach(cookie -> exchange.getResponseCookies().put(cookie.getName(), cookie));
                response.headers.forEach((name, value) -> exchange.getResponseHeaders().put(new HttpString(name), value));
                exchange.startBlocking();
                ByteStreams.copy(inputStream, exchange.getOutputStream());
            }
        } catch (Throwable e) {
            logger.error("failed to process", e);
            if (exchange.getStatusCode() == 200) {
                exchange.setStatusCode(500);
            }
        } finally {
            exchange.endExchange();
        }
    }
}